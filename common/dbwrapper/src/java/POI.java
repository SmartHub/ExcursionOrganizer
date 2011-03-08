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
        private String descr_;
        private JdbcOperations ops_;

        public Entry(final SqlRowSet poi, JdbcOperations ops) {
            id_ = poi.getLong("id");
            name_ = poi.getString("name");
            descr_ = poi.getString("descr");

            ops_ = ops;
        }

        public String name() {
            return name_;
        }

        public void setAddress(final String address) {
            ops_.execute("UPDATE " + POI_TABLE_NAME + 
                         "   SET " + POI_FIELD_ADDRESS + " = '" + address + "' " + 
                         " WHERE " + POI_FIELD_ID + " = " + String.valueOf(id_) + ";");
        }
    }


    private static class POIIterator implements Iterator {
        private JdbcOperations ops_;
        private SqlRowSet pois_;
        private boolean valid_;

        public POIIterator(JdbcOperations ops) {
            ops_ = ops;
            pois_ = ops_.queryForRowSet("SELECT " + POI_FIELD_ID + ", " + POI_FIELD_NAME + ", " + POI_FIELD_DESCR + " FROM place_of_interest;");

            valid_ = pois_.first();
        }

        public boolean hasNext() {
            return valid_;
            //return pois_.isAfterLast();
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


    public void add(final String name, final String descr) {
        //System.out.println(name + ", " + descr);

        try {
            String query = "INSERT INTO " + POI_TABLE_NAME +
                    "(" + POI_FIELD_NAME + ", " + POI_FIELD_DESCR + ")" +
                    "VALUES ('" + name + "', '" + descr + "');";

            ops_.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// ================================================================================