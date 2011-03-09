package eo.common;

import java.lang.*;
import java.util.*;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;

// ================================================================================

public class POI {
    private SimpleJdbcTemplate conn_;
    private JdbcOperations ops_;

    private static final String POI_TABLE_NAME = "place_of_interest";
    private static final String POI_FIELD_ID = "id";
    private static final String POI_FIELD_NAME = "name";
    private static final String POI_FIELD_DESCR = "descr";
    private static final String POI_FIELD_ADDRESS = "address";


    public static class Entry {
        private long id_;
        private String name_;
        private JdbcOperations ops_;

        public Entry(final SqlRowSet poi, JdbcOperations ops) {
            id_ = poi.getLong("id");
            name_ = poi.getString("name");

            ops_ = ops;
        }

        public Entry(int id, final String name, JdbcOperations ops) {
            id_ = id;
            name_ = name_;

            ops_ = ops;
        }

        public String name() {
            return name_;
        }

        public void setAddress(final String address) {
            String q = String.format(
                                     "UPDATE place_of_interest SET address='%s' WHERE id=%d;",
                                     address, id_
                                     );
            ops_.execute(q);
        }


        public void clearRawGeoInfo() {
            String q = String.format(
                                     "DELETE FROM poi_raw WHERE id=%d;",
                                     id_
                                     );
            ops_.execute(q);
        }

        public void addRawGeoInfo(final String address, double lat, double lng) {
            String q = String.format(
                                     "INSERT INTO poi_raw_geo(poi_id, address, lat, lng) VALUES (%d, '%s', %f, %f);",
                                     id_, address, lat, lng
                                     );
            ops_.execute(q);
        }

        public void addRawDescr(final String descr, final String source) {
            String q = String.format(
                                     "INSERT INTO poi_raw_descr(poi_id, descr, src_url) VALUES (%d, '%s', '%s');",
                                     id_, descr, source
                                     );
            ops_.execute(q);
        }
    }


    private static class POIIterator implements Iterator {
        private JdbcOperations ops_;
        private SqlRowSet pois_;
        private boolean valid_;

        public POIIterator(JdbcOperations ops) {
            ops_ = ops;
            pois_ = ops_.queryForRowSet("SELECT (id, name, descr) FROM place_of_interest;");

            valid_ = pois_.first();
        }

        public boolean hasNext() {
            return valid_;
        }

        public Entry next() {
            if (valid_) {
                Entry e = new Entry(pois_, ops_);
                valid_ = pois_.next();
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
        }
    }



    public POI(SimpleJdbcTemplate conn) {
        conn_ = conn;
        ops_ = conn_.getJdbcOperations();
    }


    public Iterator poiIterator() {
        return new POIIterator(ops_);
    }        


    public Entry add(final String name) throws Exception {
        //System.out.println(name + ", " + descr);

        String q = String.format(
                                 "INSERT INTO %s(%s) VALUES ('%s');",
                                 POI_TABLE_NAME, POI_FIELD_NAME, name
                                 );
        ops_.execute(q);

        int new_poi_id = ops_.queryForInt("SELECT LAST_INSERT_ID();");
        return new Entry(new_poi_id, name, ops_);
    }
}

// ================================================================================