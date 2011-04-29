package ru.exorg.core.service;
 
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import java.sql.ResultSet;

import org.json.simple.JSONObject;

import ru.exorg.core.model.*;

// ================================================================================

/**
 *  Place of Interest Service
 *  
 *  poiIterator() method returns an object that iterates over all POIs
 *                in the database. POI is incapsulated into an object
 *                of type POI.Entry.
 *
 */
final public class POIProvider {
    private DataProvider dataProvider;
    private JdbcTemplate jdbc;
    private POIMapper poiMapper;

    private Map<Long, Long> clusters;
    private long maxClusterId;

    public interface Clusters extends Map<Long, List<Long>> { }
    private class _Clusters extends TreeMap<Long, List<Long>> implements Clusters { }

    private class POIMapper implements RowMapper<POI> {
        public POI mapRow(ResultSet rs, int rowNum) throws SQLException {
            POI poi = new POI(rs.getLong("id"), rs.getString("name"));
            poi.setCityId(rs.getLong("city_id"));
            poi.setURL(rs.getString("url"));
            poi.setAddress(rs.getString("address"));
            poi.setLocation(rs.getDouble("lat"), rs.getDouble("lng"));

            SqlRowSet d_rs = jdbc.queryForRowSet("SELECT descr, src_url FROM poi_descr WHERE poi_id=?;", new Object[]{poi.getId()});
            boolean v = d_rs.first();
            while (v) {
                poi.addDescription(d_rs.getString("descr"), d_rs.getString("src_url"));
                v = d_rs.next();
            }

            d_rs = jdbc.queryForRowSet("SELECT img_url FROM poi_image WHERE poi_id=?;", new Object[]{poi.getId()});
            v = d_rs.first();
            while (v) {
                poi.addImage(d_rs.getString("img_url"));
                v = d_rs.next();
            }
            return poi;
        }
    }

    public POIProvider(DataProvider p) {
        this.dataProvider = p;
        this.jdbc = p.getJdbcTemplate();
        this.poiMapper = new POIMapper();
        this.clusters = new TreeMap<Long, Long>();
    }


    final public Iterator<POI> poiIterator() {
        return this.jdbc.query("SELECT * FROM place_of_interest;", new Object[]{}, poiMapper).iterator();
    }        

    final public List<POI> poiList() {
        return this.jdbc.query("SELECT * FROM place_of_interest;", new Object[]{}, poiMapper);
    }

    final public POI add(final String name) throws Exception {
        String q = String.format(
                                 "INSERT INTO place_of_interest(name) VALUES ('%s');",
                                 name
                                 );
        this.dataProvider.getJdbcTemplate().execute(q);

        int newPoiId = this.dataProvider.getJdbcTemplate().queryForInt("SELECT LAST_INSERT_ID();");
        return new POI(newPoiId, name);
    }

    final public void sync(final POI poi) throws Exception {
        jdbc.update(
                    "UPDATE place_of_interest SET address=?, lat=?, lng=?, city_id=?, url=?, type_id=? WHERE id=?;",
                    poi.getLocation().getAddress(),
                    poi.getLocation().getLat(),
                    poi.getLocation().getLng(),
                    poi.getCityId(),
                    poi.getURL(),
                    poi.getType(),
                    poi.getId());

        jdbc.update("DELETE FROM poi_descr WHERE poi_id=?;",
                    new Object[]{poi.getId()});
        for (Description d : poi.getDescriptions()) {
            jdbc.update(
                        "INSERT INTO poi_descr(poi_id, descr, src_url) VALUES (?, ?, ?);",
                        poi.getId(),
                        d.getText(),
                        d.getSourceURL());
        }

        jdbc.update("DELETE FROM poi_image WHERE poi_id=?;",
                    poi.getId());
        for (String img : poi.getImages()) {
            if (img.length() > 1) {
                jdbc.update(
                            "INSERT INTO poi_image(poi_id, img_url) VALUES (?, ?);",
                            poi.getId(),
                            img);
            }
        }
    }

    @SuppressWarnings("unchecked")
    final public void serializeDescriptionsAndPhotos(final POI poi) {
        JSONObject photos = new JSONObject();
        photos.put("photos", poi.getImages());

        JSONObject descrs = new JSONObject();
        descrs.put("descriptions", poi.getDescriptions());

        this.jdbc.update("UPDATE place_of_interest SET descrs=? WHERE id=?", descrs.toString(), poi.getId());
        this.jdbc.update("UPDATE place_of_interest SET photos=? WHERE id=?", photos.toString(), poi.getId());
    }

    final public void guessPOIType(POI poi) {
        try {
            int t = jdbc.queryForInt("SELECT id FROM poi_type WHERE lcase(?) rlike poi_type.guess_rx;", poi.getName());
            if (t != 0) {
                poi.setType(t);
            }
        } catch (Exception e) {
            /* Query result is empty */
            poi.setType(1);
        }
    }

    final public List<String> getPOINames() {
        class POINamesMapper implements RowMapper<String> {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        }

        return this.jdbc.query("SELECT name FROM place_of_interest;", new Object[]{}, new POINamesMapper());
    }

    final public POI queryById(long id) {
        List<POI> r = this.jdbc.query("SELECT * FROM place_of_interest WHERE id = ?", new Object[]{id}, poiMapper);
        return r.get(0);
    }

    final public POI queryByName(final String name) {
        List<POI> r = this.jdbc.query("SELECT * FROM place_of_interest WHERE name = ? LIMIT 1", new Object[]{name}, poiMapper);
        return r.get(0);
    }

    final public List<POI> queryByAddress(final String address) {
        if (address != null) {
            return this.jdbc.query("SELECT * FROM place_of_interest WHERE address = ?", new Object[]{address}, poiMapper);
        } else {
            return null;
        }
    }

    final public void removePOI(final POI poi) {
        this.jdbc.update("DELETE FROM place_of_interest WHERE id=?", poi.getId());
        this.jdbc.update("DELETE FROM poi_descr WHERE poi_id=?", poi.getId());
        this.jdbc.update("DELETE FROM poi_image WHERE poi_id=?", poi.getId());
    }

    final private boolean hasDistance(final POI poi1, final POI poi2) {
        return this.jdbc.queryForInt("SELECT COUNT(*) FROM poi_distance WHERE poi_id1=? AND poi_id2=?;", poi1.getId(), poi2.getId()) > 0;
    }

    final public void setDistance(final POI poi1, final POI poi2, double distance) {
        if (poi1.getId() < poi2.getId()) {
            if (!hasDistance(poi1, poi2)) {
                this.jdbc.update("INSERT INTO poi_distance(poi_id1, poi_id2, distance) VALUES(?, ?, ?);", poi1.getId(), poi2.getId(), distance);
            } else {
                this.jdbc.update("UPDATE poi_distance SET distance=? WHERE poi_id1=? AND poi_id2=?;", distance, poi1.getId(), poi2.getId());
            }
        }
    }


    final public void clearClusters() {
        this.clusters.clear();
        this.maxClusterId = 1;
    }

    final public boolean isInCluster(final POI poi) {
        return clusters.containsKey(poi.getId());
    }

    final public long getPOICluster(final POI poi) {
        if (this.clusters.containsKey(poi.getId())) {
            return this.clusters.get(poi.getId());
        } else {
            return 0;
        }
    }

    final public long getMaxClusterId() {
        return this.maxClusterId;
    }

    final public void setPOICluster(final POI poi, long clusterId) {
        if (clusterId > 0) {
            this.clusters.put(poi.getId(), clusterId);

            if (clusterId > this.maxClusterId) {
                this.maxClusterId = clusterId;
            }
        } else {
            this.clusters.put(poi.getId(), this.maxClusterId + 1);
            this.maxClusterId++;
        }
    }

    final public void removeFromCluster(final POI poi) {
        this.clusters.remove(poi.getId());
    }

    final public Clusters getClusters() {
        Clusters icl = new _Clusters();

        for (Map.Entry<Long, Long> e : clusters.entrySet()) {
            if (!icl.containsKey(e.getValue())) {
                icl.put(e.getValue(), new ArrayList<Long>());
            }

            icl.get(e.getValue()).add(e.getKey());
        }

        return icl;
    }

    final public void collapseClusters() throws Exception {
        Clusters icl = this.getClusters();

        for (Map.Entry<Long, List<Long>> e : icl.entrySet()) {
            if (e.getValue().size() >= 2) {
                Iterator<Long> it = e.getValue().iterator();
                POI cur = this.queryById(it.next());

                System.out.println("Merging:");
                while (it.hasNext()) {
                    POI other = this.queryById(it.next());
                    cur.addDescriptions(other.getDescriptions());
                    cur.addImages(other.getImages());

                    this.removePOI(other);

                    System.out.println(other.getName() + " " + String.valueOf(other.getId()));
                }

                System.out.println(cur.getName() + " " + String.valueOf(cur.getId()) + "\n\n");
                this.sync(cur); // Atomicity?
            }
        }

        this.clearClusters();
    }
}

// ================================================================================