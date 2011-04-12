package eo.common;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import java.sql.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.PreparedStatementCreator;

// ================================================================================

public class DataProvider {
    private JdbcTemplate jdbc;
    private Connection conn;

    private POIProvider poiProvider;
    private CafeProvider cafeProvider;

    public static class Loc {
        public double lat;
        public double lng;

        public Loc(double lat_i, double lng_i) {
            this.lat = lat_i;
            this.lng = lng_i;
        }
    }

    public static class Address {
        public String address;
        public Loc loc;

        public Address(final SqlRowSet rs) {
            this.address = rs.getString("address");
            this.loc = new Loc(rs.getDouble("lat"), rs.getDouble("lng"));
        }
    }

    /*
    protected static class RSIterator<T> implements Iterator<T> {
        private DataProvider dataProvider;
        private SqlRowSet rs;
        private Class<T> cls;
        private boolean valid;

        public RSIterator(DataProvider p, final String q) {
            this.dataProvider = p;
            this.rs = this.dataProvider.getJdbcOperations().queryForRowSet(q);
            this.valid = this.rs.first();
        }

        public boolean hasNext() {
            return this.valid;
        }

        public T next() {
            if (valid) {
                Constructor<T> c = new Constructor<T>();
                T e = c.newInstance(this.rs, this.dataProvider);
                this.valid = rs.next();
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
        }
    }
    */

    public DataProvider(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbc = jdbcTemplate;
        this.conn = this.jdbc.getDataSource().getConnection();

        this.poiProvider = new POIProvider(this);
        this.cafeProvider = new CafeProvider(this);
    }

    public POIProvider getPOIProvider() {
        return poiProvider;
    }

    public CafeProvider getCafeProvider() {
        return cafeProvider;
    }

    public JdbcOperations getJdbcOperations() {
        return jdbc;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean isWithinCity(long city_id, Loc loc) {
        String q = String.format(
                                 "SELECT sw_lat, sw_lng, ne_lat, ne_lng FROM city WHERE id=%d;",
                                 city_id
                                 );

        SqlRowSet rs = jdbc.queryForRowSet(q);
        rs.first();
        return (loc.lat >= rs.getDouble("sw_lat") && loc.lat <= rs.getDouble("ne_lat")) 
            && 
            (loc.lng >= rs.getDouble("sw_lng") && loc.lng <= rs.getDouble("ne_lng"));
    }

    public long getCityId(final String cityName) throws Exception {
        PreparedStatement s 
            = conn.prepareStatement("SELECT id FROM city WHERE name=?;");
        s.setString(1, cityName);

        ResultSet rs = s.executeQuery();
        rs.first();
        return rs.getLong("id");
    }
}
