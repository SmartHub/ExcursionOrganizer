package ru.exorg.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Connection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ru.exorg.model.*;

// ================================================================================

public class CafeProvider {
    private DataProvider dataProvider;
    private JdbcTemplate jdbc;
    private Connection conn;

    private static class CafeIterator implements Iterator<Cafe> {
        private DataProvider dataProvider;
        private SqlRowSet rs;
        private boolean valid;

        private Cafe extractCafe() {
            Cafe cafe = new Cafe(rs.getLong("id"), rs.getString("name"));
            cafe.setCityId(rs.getLong("city_id"));
            JdbcTemplate jdbc = dataProvider.getJdbcTemplate();
            SqlRowSet address_rs = jdbc.queryForRowSet("SELECT address, lat, lng FROM cafe_address WHERE cafe_id=?;", 
                                                       new Object[]{cafe.getId()});
            boolean v = address_rs.first();
            while (v) {
                cafe.addLocation(address_rs.getString("address"), address_rs.getDouble("lat"), address_rs.getDouble("lng"));
                v = address_rs.next();
            }

            return cafe;
        }

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
                Cafe e = extractCafe();
                this.valid = rs.next();
                return e;
            } else {
                throw new NoSuchElementException();
            }
        }

        public void remove() { }
    }

    public CafeProvider(DataProvider p) {
        this.dataProvider = p;
        this.jdbc = p.getJdbcTemplate();
    }

    private static String escape(final String s) {
        return s.replaceAll("'", "\\\\'").replaceAll("\"", "\\\"");
    }

    public Cafe add(final String name) throws Exception {
        jdbc.update("INSERT INTO cafe(name) VALUES (?);",
                    new Object[]{name});
        int newCafeId = this.dataProvider.getJdbcTemplate().queryForInt("SELECT LAST_INSERT_ID();");
        return new Cafe(newCafeId, name);
    }

    public Iterator<Cafe> cafeIterator() {
        return new CafeIterator(dataProvider, "SELECT * FROM cafe;");
    }

    public void sync(final Cafe cafe) {
        this.jdbc.update(
                         "UPDATE cafe SET city_id=?, descr=?, descr_src=?, url=?, cuisine=? WHERE id=?;",
                         new Object[] {
                             cafe.getCityId(),
                             cafe.getDescription().getText(),
                             cafe.getDescription().getSourceURL(),
                             cafe.getURL(),
                             cafe.getCuisine(),
                             cafe.getId()
                         });

        this.jdbc.update("DELETE FROM cafe_address WHERE cafe_id=?;", cafe.getId());
        for (Location loc : cafe.getLocations()) {
            this.jdbc.update(
                             "INSERT INTO cafe_address(cafe_id, city_id, address, lat, lng) VALUES (?, ?, ?, ?, ?);",
                             new Object[] {
                                 cafe.getId(),
                                 loc.getCityId(),
                                 loc.getAddress(),
                                 loc.getLat(),
                                 loc.getLng()
                             });
        }
    }
}