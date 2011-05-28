package ru.exorg.core.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.exorg.core.model.Location;
import ru.exorg.core.model.POI;

import java.lang.Object;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import ru.exorg.core.model.City;
import ru.exorg.core.model.PoiType;
import ru.exorg.core.model.LatLng;

// ================================================================================

public class DataProvider implements InitializingBean {
    private JdbcTemplate jdbc;

    private POIProvider poiProvider;
    private CafeProvider cafeProvider;

    private static CityMapper cityMapper = new CityMapper();
    private static POITypeMapper poiTypeMapper = new POITypeMapper();

    private static class CityMapper implements RowMapper<City> {
        public City mapRow(ResultSet rs, int rowNum) throws SQLException {
            City city = new City();

            city.setId(rs.getLong("id"));
            city.setName(rs.getString("name"));
            city.setNeLatLng(new LatLng(rs.getDouble("ne_lat"), rs.getDouble("ne_lng")));
            city.setSwLatLng(new LatLng(rs.getDouble("sw_lat"), rs.getDouble("sw_lng")));
            city.setLatSubdivs(rs.getInt("lat_subdiv"));
            city.setLngSubdivs(rs.getInt("lng_subdiv"));

            return city;
        }
    }

    private static class POITypeMapper implements RowMapper<PoiType> {
        public PoiType mapRow(ResultSet rs, int i) throws SQLException {
            return new PoiType(rs.getLong("id"), rs.getString("name"), rs.getString("icon"));
        }
    }

    public DataProvider() { }

    public DataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
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

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }

    public boolean isWithinCity(long cityId, final Location loc) {
        City city = this.queryCity(cityId);

        LatLng sw = city.getSwLatLng();
        LatLng ne = city.getNeLatLng();

        return 
            (loc.getLat() >= sw.getLat() && loc.getLat() <= ne.getLat())
            && 
            (loc.getLng() >= sw.getLng() && loc.getLng() <= ne.getLng());
    }

    public long getCityId(final String cityName) throws Exception {
        return jdbc.queryForLong(
                                 "SELECT id FROM city WHERE name=?;",
                                 new Object[]{cityName});
    }

    public City queryCity(long cityId) {
        return this.jdbc.query("SELECT * FROM city WHERE id=?;", new Object[]{cityId}, cityMapper).get(0);
    }

    public void guessPOIType(POI poi) {
        try {
            int t = this.jdbc.queryForInt("SELECT id FROM poi_type WHERE lcase(?) rlike poi_type.guess_rx;",
                                          poi.getName());
            if (t != 0) {
                poi.setType(t);
            }
        } catch (Exception e) {
            /* Query result is empty */
            poi.setType(1);
        }
    }

    public List<PoiType> getPoiTypes() throws Exception {
        return this.jdbc.query("SELECT * FROM poi_type", poiTypeMapper);
    }

    public PoiType getPoiType(long typeId) {
        return this.jdbc.queryForObject("SELECT * FROM poi_type WHERE id=?", poiTypeMapper, new Object[]{typeId});
    }

    public void afterPropertiesSet() {
        this.poiProvider = new POIProvider(this);
    }
}
