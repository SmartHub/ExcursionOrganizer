package eo.db;

import java.lang.*;
import java.util.*;
import java.sql.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.PreparedStatementCreator;

import eo.model.*;

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

    private static class POIIterator implements Iterator<POI> {
        private DataProvider dataProvider;
        private JdbcTemplate jdbc;
        private SqlRowSet rs;
        private boolean valid;

        private POI extractPOI() {
            POI poi = new POI(rs.getLong("id"), rs.getString("name"));
            poi.setCityId(rs.getLong("city_id"));
            poi.setURL(rs.getString("url"));

            SqlRowSet d_rs = this.jdbc.queryForRowSet("SELECT descr, src_url FROM poi_raw_descr WHERE poi_id=?;", new Object[]{poi.getId()});
            boolean v = d_rs.first();
            while (v) {
                poi.addDescription(d_rs.getString("descr"), d_rs.getString("src_url"));
                v = d_rs.next();
            }
            return poi;
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


    public POIProvider(DataProvider p) {
        this.dataProvider = p;
        this.jdbc = p.getJdbcTemplate();
    }

    final public Iterator<POI> poiIterator() {
        return new POIIterator(this.dataProvider, "SELECT id, name, address, city_id FROM place_of_interest;");
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
                    new Object[] {
                        poi.getLocation().getAddress(),
                        poi.getLocation().getLat(),
                        poi.getLocation().getLng(),
                        poi.getCityId(),
                        poi.getURL(),
                        poi.getType(),
                        poi.getId()
                    });

        for (Description d : poi.getDescriptions()) {
            jdbc.update(
                        "INSERT INTO poi_descr(poi_id, descr, src_url) VALUES (?, ?, ?);",
                        new Object[] {
                            poi.getId(),
                            d.getText(),
                            d.getSourceURL()
                        });
        }

        for (String img : poi.getImages()) {
            if (img.length() > 1) {
                jdbc.update(
                            "INSERT INTO poi_image(poi_id, img_url) VALUES (?, ?);",
                            new Object[] {
                                poi.getId(),
                                img
                            });
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
}

// ================================================================================