package eo.db;

import java.lang.*;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import eo.model.*;

// ================================================================================

public class DataProvider {
    private JdbcTemplate jdbc;
    private Connection conn;

    private POIProvider poiProvider;
    private CafeProvider cafeProvider;

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

    public boolean isWithinCity(long cityId, final Location loc) {
        SqlRowSet rs = 
            jdbc.queryForRowSet(
                                "SELECT sw_lat, sw_lng, ne_lat, ne_lng FROM city WHERE id=?;",
                                new Object[]{cityId}
                                );
        rs.first();

        double swLat = rs.getDouble("sw_lat");
        double neLat = rs.getDouble("ne_lat");
        double swLng = rs.getDouble("sw_lng");
        double neLng = rs.getDouble("ne_lng");
        return 
            (loc.getLat() >= swLat && loc.getLat() <= neLat) 
            && 
            (loc.getLng() >= swLng && loc.getLng() <= neLng);
    }

    public long getCityId(final String cityName) throws Exception {
        return jdbc.queryForLong(
                                 "SELECT id FROM city WHERE name=?;",
                                 new Object[]{cityName});
    }
}
