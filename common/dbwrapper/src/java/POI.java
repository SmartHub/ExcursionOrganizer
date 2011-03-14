package eo.common;

import java.lang.*;
import java.util.*;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;

// ================================================================================

/**
 *  Place of Interest Service
 *  
 *  poiIterator() method returns an object that iterates over all POIs
 *                in the database. POI is incapsulated into an object
 *                of type POI.Entry.
 *
 */
public class POI {
    private SimpleJdbcTemplate conn_;
    private JdbcOperations ops_;

    private static final String POI_TABLE_NAME = "place_of_interest";
    private static final String POI_FIELD_ID = "id";
    private static final String POI_FIELD_NAME = "name";
    private static final String POI_FIELD_DESCR = "descr";
    private static final String POI_FIELD_ADDRESS = "address";

    public static class Loc {
        public double lat;
        public double lng;

        public Loc(double lat_i, double lng_i) {
            lat = lat_i;
            lng = lng_i;
        }
    }

    public static class Description {
        public String text;
        public String source_url;

        public Description(final String t, final String s) {
            text = t;
            source_url = s;
        }
    }

    /*
    public static class City {
        private int id_;
        private Loc ne_, sw_;

        City(final SqlRowSet city) {
            id_.
        }
    }
    */

    /** 
     *  A place of interest item.
     * 
     *  Indexer interface:
     *  getName() - returns a name of the POI.
     *  
     *  getDescriptions() - returns a list of POI descriptions.
     *
     *  getLocation() - returns a locatoin of the POI.
     */
    public static class Entry {
        private long id_;
        private long city_id_;
        private String name_;
        private String address_;

        private JdbcOperations ops_;

        /**
         *  Constructs a POI item from a rowset. 
         *  Used internally within a POI iterator
         */
        public Entry(final SqlRowSet poi, JdbcOperations ops) {
            id_ = poi.getLong("id");
            name_ = poi.getString("name");
            city_id_ = poi.getLong("city_id");

            address_ = poi.getString("address");
            if (poi.wasNull()) {
                address_ = "";
            }

            ops_ = ops;
        }

        /**
         *  Constructs a POI item, given a name.
         *  Used iternally.
         */
        public Entry(int id, final String name, JdbcOperations ops) {
            id_ = id;
            name_ = name_;
            address_ = "";

            ops_ = ops;
        }

        /**
         *  Returns a name of POI
         */
        public String getName() {
            return name_;
        }

        /**
         *  Returns a list of POI descriptions
         */
        public List<Description> getDescription() {
            String q = String.format(
                                     "SELECT descr, src_url FROM poi_raw_descr WHERE poi_id=%d;",
                                     id_
                                     );

            SqlRowSet rs = ops_.queryForRowSet(q);
            List<Description> d = new ArrayList<Description>();

            if (rs.first()) {
                do {
                    d.add(new Description(rs.getString("descr"), rs.getString("src_url")));
                } while (rs.next());
            }

            return d;
        }

        /* Clean GEO information management */

        /**
         *  Address management routines
         */
        public String getAddress() {
            return address_;
        }

        public void setAddress(final String address) {
            String q = String.format(
                                     "UPDATE place_of_interest SET address='%s' WHERE id=%d;",
                                     address, id_
                                     );
            ops_.execute(q);
        }

        public boolean hasAddress() {
            return address_.length() > 1;
        }

        // --------------------------------------------------------------------------------

        public Loc getLocation() {
            String q = String.format(
                                     "SELECT lat, lng FROM poi_raw_geo WHERE poi_id=%d LIMIT 1;",
                                     id_
                                     );

            SqlRowSet rs = ops_.queryForRowSet(q);
            rs.first();

            return new Loc(rs.getDouble("lat"), rs.getDouble("lng"));
        }

        // --------------------------------------------------------------------------------

        /**
         *  City management routines
         */
        public long getCityId() {
            return city_id_;
        }

        public void setCity(final String city) {
            String q = String.format(
                                     "SELECT id FROM city WHERE name='%s';",
                                     city
                                     );

            city_id_ = ops_.queryForInt(q);
            q = String.format(
                              "UPDATE place_of_interest SET city_id=%d WHERE id=%d;",
                              city_id_, id_
                              );
            ops_.execute(q);
        }

        // --------------------------------------------------------------------------------

        public void setURL(final String url) {
            String q = String.format(
                                     "UPDATE place_of_interest SET url='%s' WHERE id=%d;",
                                     url, id_
                                     );
            ops_.execute(q);
        }

        // --------------------------------------------------------------------------------

        /**
         *  Type management 
         */
        public boolean hasType() {
            String q = String.format(
                                     "SELECT type_id FROM place_of_interest WHERE id=%d;",
                                     id_
                                     );

            int t = ops_.queryForInt(q);
            return t != 0 && t != 1;
        }

        public void setType(int t) {
            String q = String.format(
                                     "UPDATE place_of_interest SET type_id = %d WHERE id = %d;",
                                     t, id_
                                     );

            ops_.execute(q);
        }

        public void guessType() {
            String q = String.format(
                                     "SELECT id FROM poi_type WHERE lcase('%s') rlike poi_type.guess_rx;",
                                     name_
                                     );
            try {
                int t = ops_.queryForInt(q);
                if (t != 0) {
                    setType(t);
                }
            } catch (Exception e) {
                /* Query result is empty */
                setType(1);
            }
        }

        // --------------------------------------------------------------------------------


        /* 
           Raw GEO information management. 
           Intended to be used in the processing and miner modules
        */
        public boolean hasRawGeo() {
            String q = String.format(
                                     "SELECT COUNT(*) FROM poi_raw_geo WHERE poi_id=%d;",
                                     id_
                                     );

            return ops_.queryForInt(q) > 0;
        }


        public void clearRawGeoInfo() {
            String q = String.format(
                                     "DELETE FROM poi_raw WHERE id=%d;",
                                     id_
                                     );
            ops_.execute(q);
        }

        /* Localization is implemented differently everywhere :( */
        private static String hack(double d) {
            return String.valueOf(d).replace(',', '.');
        }

        public void addRawGeoInfo(final String address, double lat, double lng) {
            String q = String.format(
                                     "INSERT INTO poi_raw_geo(poi_id, address, lat, lng) VALUES (%d, '%s', '%s', '%s');",
                                     id_, address, hack(lat), hack(lng)
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

    // --------------------------------------------------------------------------------

    private static class POIIterator implements Iterator {
        private JdbcOperations ops_;
        private SqlRowSet pois_;
        private boolean valid_;

        public POIIterator(JdbcOperations ops) {
            ops_ = ops;
            pois_ = ops_.queryForRowSet("SELECT id, name, address, city_id FROM place_of_interest;");

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

    // --------------------------------------------------------------------------------

    public POI(SimpleJdbcTemplate conn) {
        conn_ = conn;
        ops_ = conn_.getJdbcOperations();
    }


    public Iterator poiIterator() {
        return new POIIterator(ops_);
    }        


    public Entry add(final String name) throws Exception {
        String q = String.format(
                                 "INSERT INTO %s(%s) VALUES ('%s');",
                                 POI_TABLE_NAME, POI_FIELD_NAME, name
                                 );
        ops_.execute(q);

        int new_poi_id = ops_.queryForInt("SELECT LAST_INSERT_ID();");
        return new Entry(new_poi_id, name, ops_);
    }

    // --------------------------------------------------------------------------------

    public boolean isWithinCity(long city_id, Loc loc) {
        String q = String.format(
                                 "SELECT sw_lat, sw_lng, ne_lat, ne_lng FROM city WHERE id=%d;",
                                 city_id
                                 );

        SqlRowSet rs = ops_.queryForRowSet(q);
        rs.first();
        return (loc.lat >= rs.getDouble("sw_lat") && loc.lat <= rs.getDouble("ne_lat")) 
            && 
            (loc.lng >= rs.getDouble("sw_lng") && loc.lng <= rs.getDouble("ne_lng"));
    }

    public long getCityId(final String city_name) {
        String q = String.format(
                                 "SELECT id FROM city WHERE name='%s';",
                                 city_name
                                 );

        return ops_.queryForLong(q);
    }

    /*
    public Map<Integer, List<String>> getTypeKeywords() {
        SqlRowSet rs = ops_.queryForRowSet("SELECT id, name FROM poi_type UNION SELECT type_id id, keyword FROM poi_type_heuristics;");
        boolean valid = rs.first();
        Map<Integer, List<String>> res = new TreeMap<Integer, List<String>>();
        
        while (valid) {
            int type_id = rs.getInt("id");

            if (res.get(type_id) == null) {
                res.put(type_id, new ArrayList<String>());
            }

            res.get(type_id).add(rs.getString("name"));

            valid = rs.next();
        }

        return res;
    }
    */
}

// ================================================================================