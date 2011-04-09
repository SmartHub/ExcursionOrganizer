package eo.common;

import java.lang.*;
import java.util.*;
import java.sql.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.PreparedStatementCreator;

// ================================================================================

public class CafeProvider {
    private DataProvider dataProvider;
    private JdbcTemplate jdbc;
    private Connection conn;

    public static class Cafe {
        private DataProvider dataProvider;
        private long id;
        private String name;
        private String address;
        private String descr;
        private String descrSource;
        private DataProvider.Address[] addresses;

        public Cafe(SqlRowSet rs, DataProvider p) {
            this.dataProvider = p;
           
            this.id = rs.getLong("id");
            //this.address = rs.getString("address");

            String q = String.format("SELECT COUNT(*) FROM cafe_address WHERE cafe_id=%d",
                                     this.id);
            int ac = p.getJdbcTemplate().queryForInt(q);
            if (ac > 0) {
                this.addresses = new DataProvider.Address[ac];

                q = String.format("SELECT address, lat, lng FROM cafe_address WHERE cafe_id=%d",
                                  this.id);
                SqlRowSet address_rs = p.getJdbcTemplate().queryForRowSet(q);
                boolean v = address_rs.first();
                for (int i = 0; i < ac && v; ++i) {
                    this.addresses[i] = new DataProvider.Address(address_rs);
                    v = address_rs.next();
                }
            }
        }

        public Cafe(long cafeId, final String name, DataProvider p) {
            this.dataProvider = p;
            this.id = cafeId;
            this.name = name;
        }

        public void setAddress(final String addr) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("INSERT INTO cafe_address(cafe_id, address) VALUES (?, ?);");
            s.setLong(1, this.id);
            s.setString(2, addr);            

            s.execute();
        }

        public DataProvider.Address[] getAddresses() {
            return this.addresses;
        }

        public String getAddress() {
            String q = String.format("SELECT address FROM cafe_address WHERE cafe_id=%d LIMIT 1;",
                                     this.id);

            return (String)dataProvider.getJdbcTemplate().queryForObject(q, String.class);
        }

        public boolean hasAddress() {
            String q = String.format("SELECT COUNT(*) FROM cafe_address WHERE id=%d;",
                                     this.id);

            return dataProvider.getJdbcTemplate().queryForLong(q) > 0;
        }

        public void setDescription(final String d, final String src) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("UPDATE cafe SET descr=?, descr_src=? WHERE id=?;");
            s.setLong(3, this.id);
            s.setString(1, d);
            s.setString(2, src);

            s.execute();
        }

        public void setCuisine(final String cuisine) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("UPDATE cafe SET cuisine=? WHERE id=?;");
            s.setLong(2, this.id);
            s.setString(1, cuisine);

            s.execute();
        }

        public void setURL(final String url) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("UPDATE cafe SET url=? WHERE id=?;");
            s.setLong(2, this.id);
            s.setString(1, url);

            s.execute();
        }

        public void setCity(final String city) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("UPDATE cafe SET city_id=? WHERE id=?;");
            s.setLong(2, this.id);
            s.setLong(1, dataProvider.getCityId(city));

            s.execute();
        }

        public long getCityId() {
            String q = String.format("SELECT city_id FROM cafe WHERE id=%d;",
                                     this.id);

            return dataProvider.getJdbcTemplate().queryForLong(q);
        }

        public boolean hasGeo(final String address) {
            String q = String.format("SELECT COUNT(*) FROM cafe_address WHERE cafe_id=%d AND address='%s' AND NOT (lat IS NULL);",
                                     this.id, address);
            return dataProvider.getJdbcTemplate().queryForInt(q) > 0;
        }

        public void setGeoInfo(final String old_address, final String new_address, double lat, double lng) throws Exception {
            PreparedStatement s = 
                dataProvider.getConnection().prepareStatement("UPDATE cafe_address SET address=?, lat=?, lng=? WHERE cafe_id=? AND address=?;");
            s.setString(1, new_address);
            s.setDouble(2, lat);
            s.setDouble(3, lng);
            s.setLong(4, this.id);
            s.setString(5, old_address);

            s.execute();           
        }
    }

    public CafeProvider(DataProvider p) throws Exception {
        this.dataProvider = p;
    }

    private static String escape(final String s) {
        return s.replaceAll("'", "\\\\'").replaceAll("\"", "\\\"");
    }

    public Cafe add(final String name) throws Exception {
        String q = String.format("INSERT INTO cafe(name) VALUES ('%s');",
                                 escape(name));
        this.dataProvider.getJdbcTemplate().execute(q);

        int newCafeId = this.dataProvider.getJdbcTemplate().queryForInt("SELECT LAST_INSERT_ID();");
        return new Cafe(newCafeId, name, this.dataProvider);
    }


    private static class CafeIterator implements Iterator<Cafe> {
        private DataProvider dataProvider;
        private SqlRowSet rs;
        private boolean valid;

        public CafeIterator(DataProvider p, final String q) {
            this.dataProvider = p;
            this.rs = this.dataProvider.getJdbcOperations().queryForRowSet(q);
            this.valid = this.rs.first();
        }

        public boolean hasNext() {
            return this.valid;
        }

        public Cafe next() {
            if (valid) {
                Cafe e = new Cafe(this.rs, this.dataProvider);
                this.valid = rs.next();
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
        }
    }

    public Iterator<Cafe> cafeIterator() {
        return new CafeIterator(dataProvider, "SELECT id FROM cafe;");
    }
}