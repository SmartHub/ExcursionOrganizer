package ru.exorg.service;

import ru.exorg.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;

import java.sql.ResultSet;

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
            return poi;
        }
    }

    /*
    private static POI extractPOI(final ResultSet rs) throws SQLException {

        return poi;
    }
    */

    /*
    private static class POIIterator implements Iterator<POI> {
        private DataProvider dataProvider;
        private JdbcTemplate jdbc;
        private SqlRowSet rs;
        private boolean valid;

        private POI extractPOI() {

        }

        public POIIterator(DataProvider p, final String q) {
            this.dataProvider = p;
            this.jdbc = p.getJdbcTemplate();
            this.rs = this.dataProvider.getJdbcOperations().queryForRowSet(q);
            this.valid = this.rs.first();
        }

        public boolean hasNext() {
            return this.valid;
        }

        public POI next() {
            if (valid) {
                POI poi = extractPOI();
                this.valid = rs.next();
                return poi;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() { }
    }
    */


    public POIProvider(DataProvider p) {
        this.dataProvider = p;
        this.jdbc = p.getJdbcTemplate();
        this.poiMapper = new POIMapper();
    }


    final public Iterator<POI> poiIterator() {
        return this.jdbc.query("SELECT * FROM place_of_interest;", new Object[]{}, poiMapper).iterator();
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

    final boolean isInCluster(final POI poi) {
        return this.jdbc.queryForInt("SELECT COUNT(*) FROM poi_cluster WHERE poi_id = ?;", poi.getId()) != 0;
    }

    final public long getPOICluster(final POI poi) {
        if (this.isInCluster(poi)) {
            return this.jdbc.queryForLong("SELECT id FROM poi_cluster WHERE poi_id=? LIMIT 1;", poi.getId());
        } else {
            return 0;
        }
    }

    final public void setPOICluster(final POI poi, long clusterId) {
        this.jdbc.update("DELETE FROM poi_cluster WHERE poi_id = ?;", poi.getId());

        if (clusterId > 0) {
            this.jdbc.update("INSERT INTO poi_cluster(id, poi_id) VALUES (?, ?)", clusterId, poi.getId());
        } else {
            long maxClusterId = this.jdbc.queryForInt("SELECT MAX(id) FROM poi_cluster;");
            this.jdbc.update("INSERT INTO poi_cluster(id, poi_id) VALUES (?, ?)", maxClusterId + 1, poi.getId());
        }
    }

    final public void clearClusters() {
        this.jdbc.update("TRUNCATE poi_cluster");
    }

    final public List<String> getPOINames() {
        class POINamesMapper implements RowMapper<String> {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("name");
            }
        }

        return this.jdbc.query("SELECT name FROM place_of_interest;", new Object[]{}, new POINamesMapper());
    }

    final public POI queryByName(final String name) {
        List<POI> r = this.jdbc.query("SELECT * FROM place_of_interest WHERE name = ? LIMIT 1", new Object[]{name}, poiMapper);
        return r.get(0);
    }
}

// ================================================================================