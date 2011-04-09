package eo.common;

import java.lang.*;
import java.util.*;
import java.sql.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.PreparedStatementCreator;

// ================================================================================

/**
 *  Place of Interest Service
 *  
 *  poiIterator() method returns an object that iterates over all POIs
 *                in the database. POI is incapsulated into an object
 *                of type POI.Entry.
 *
 */
public class POIProvider {
    private DataProvider dataProvider;

    public static class Description {
        public String text;
        public String source_url;

        public Description(final String t, final String s) {
            text = t;
            source_url = s;
        }
    }

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
        private long id;
        private long city_id;
        private String name_;
        private String address_;

        private JdbcTemplate jdbc;

        /**
         *  Constructs a POI item from a rowset. 
         *  Used internally within a POI iterator
         */
        public Entry(final SqlRowSet poi, DataProvider p) {
            this.id = poi.getLong("id");
            this.name_ = poi.getString("name");
            this.city_id = poi.getLong("city_id");

            this.address_ = poi.getString("address");
            if (poi.wasNull()) {
                address_ = "";
            }

            this.jdbc = p.getJdbcTemplate();
        }

        /**
         *  Constructs a POI item, given a name.
         *  Used iternally.
         */
        public Entry(int id, final String name, DataProvider p) {
            this.id = id;
            this.name_ = name_;
            this.address_ = "";

            this.jdbc = p.getJdbcTemplate();
        }

        /**
         *  Returns a name of POI
         */
        public String getName() {
            return this.name_;
        }

        /**
         *  Returns a list of POI descriptions
         */
        public List<Description> getDescription() {
            String q = String.format(
                                     "SELECT descr, src_url FROM poi_raw_descr WHERE poi_id=%d;",
                                     this.id
                                     );

            SqlRowSet rs = this.jdbc.queryForRowSet(q);
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
            return this.address_;
        }

        public void setAddress(final String address) {
            String q = String.format(
                                     "UPDATE place_of_interest SET address='%s' WHERE id=%d;",
                                     address, id
                                     );
            this.jdbc.execute(q);
        }

        public boolean hasAddress() {
            return this.address_.length() > 1;
        }

        // --------------------------------------------------------------------------------

        public DataProvider.Loc getLocation() {
            String q = String.format(
                                     "SELECT lat, lng FROM poi_raw_geo WHERE poi_id=%d LIMIT 1;",
                                     id
                                     );

            SqlRowSet rs = jdbc.queryForRowSet(q);
            rs.first();

            return new DataProvider.Loc(rs.getDouble("lat"), rs.getDouble("lng"));
        }

        // --------------------------------------------------------------------------------

        /**
         *  City management routines
         */
        public long getCityId() {
            return this.city_id;
        }

        public void setCity(final String city) {
            String q = String.format(
                                     "SELECT id FROM city WHERE name='%s';",
                                     city
                                     );

            this.city_id = jdbc.queryForInt(q);
            q = String.format(
                              "UPDATE place_of_interest SET city_id=%d WHERE id=%d;",
                              this.city_id, this.id
                              );
            this.jdbc.execute(q);
        }

        // --------------------------------------------------------------------------------

        public void setURL(final String url) {
            String q = String.format(
                                     "UPDATE place_of_interest SET url='%s' WHERE id=%d;",
                                     url, this.id
                                     );
            this.jdbc.execute(q);
        }

        // --------------------------------------------------------------------------------

        /**
         *  Type management 
         */
        public boolean hasType() {
            String q = String.format(
                                     "SELECT type_id FROM place_of_interest WHERE id=%d;",
                                     id
                                     );

            int t = this.jdbc.queryForInt(q);
            return t != 0 && t != 1;
        }

        public void setType(int t) {
            String q = String.format(
                                     "UPDATE place_of_interest SET type_id = %d WHERE id = %d;",
                                     t, this.id
                                     );

            this.jdbc.execute(q);
        }

        public void guessType() {
            String q = String.format(
                                     "SELECT id FROM poi_type WHERE lcase('%s') rlike poi_type.guess_rx;",
                                     name_
                                     );
            try {
                int t = this.jdbc.queryForInt(q);
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
                                     id
                                     );

            return this.jdbc.queryForInt(q) > 0;
        }


        public void clearRawGeoInfo() {
            String q = String.format(
                                     "DELETE FROM poi_raw WHERE id=%d;",
                                     id
                                     );
            this.jdbc.execute(q);
        }

        /* Localization is implemented differently everywhere :( */
        private static String hack(double d) {
            return String.valueOf(d).replace(',', '.');
        }

        public void addRawGeoInfo(final String address, double lat, double lng) {
            String q = String.format(
                                     "INSERT INTO poi_raw_geo(poi_id, address, lat, lng) VALUES (%d, '%s', '%s', '%s');",
                                     id, address, hack(lat), hack(lng)
                                     );
            this.jdbc.execute(q);
        }

        public void addRawDescr(final String descr, final String source) throws Exception {
            PreparedStatement s = 
                jdbc.getDataSource().getConnection().prepareStatement("INSERT INTO poi_raw_descr(poi_id, descr, src_url) VALUES (?, ?, ?);");

            s.setLong(1, this.id);
            s.setString(2, descr);
            s.setString(3, source);

            s.execute();
        }

        public void addRawImages(final String[] imgs) {
            for (int i = 0; i < imgs.length; ++i) {
                if (imgs[i].length() > 1) {
                    String q = String.format(
                                             "INSERT INTO poi_raw_images(poi_id, img_url) VALUES (%d, '%s');",
                                             this.id, imgs[i]
                                             );
                    this.jdbc.execute(q);
                }
            }
        }
    }

    // --------------------------------------------------------------------------------

    public POIProvider(DataProvider p) {
        this.dataProvider = p;
    }

    private static class POIIterator implements Iterator<Entry> {
        private DataProvider dataProvider;
        private SqlRowSet rs;
        private boolean valid;

        public POIIterator(DataProvider p, final String q) {
            this.dataProvider = p;
            this.rs = this.dataProvider.getJdbcOperations().queryForRowSet(q);
            this.valid = this.rs.first();
        }

        public boolean hasNext() {
            return this.valid;
        }

        public Entry next() {
            if (valid) {
                Entry e = new Entry(this.rs, this.dataProvider);
                this.valid = rs.next();
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
        }
    }

    public Iterator<Entry> poiIterator() {
        return new POIIterator(this.dataProvider, "SELECT id, name, address, city_id FROM place_of_interest;");
    }        


    public Entry add(final String name) throws Exception {
        String q = String.format(
                                 "INSERT INTO place_of_interest(name) VALUES ('%s');",
                                 name
                                 );
        this.dataProvider.getJdbcTemplate().execute(q);

        int newPoiId = this.dataProvider.getJdbcTemplate().queryForInt("SELECT LAST_INSERT_ID();");
        return new Entry(newPoiId, name, this.dataProvider);
    }
}

// ================================================================================