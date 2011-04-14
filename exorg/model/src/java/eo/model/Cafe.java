package eo.model;

import java.lang.*;
import java.util.*;

// ================================================================================

public class Cafe {
    private long id;
    private String name;
    private String url;
    private String cuisine;
    private Description description;

    private List<Location> locations;

    public Cafe(long cafeId, final String name) {
        this.id = cafeId;
        this.name = name;

        this.locations = new ArrayList<Location>();
        this.description = new Description("N/A", "N/A");
    }

    final public long getId() {
        return this.id;
    }

    final public String getName() {
        return this.name;
    }

    final public Description getDescription() {
        return this.description;
    }

    final public void setDescription(final String text, final String source) {
        this.description = new Description(text, source);
    }

    final public List<Location> getLocations() {
        return this.locations;
    }

    final public void addLocation(long cityId, final String address, double lat, double lng) {
        this.locations.add(new Location(cityId, address, lat, lng));
    }

    final public void addLocation(long cityId, final String address) {
        this.locations.add(new Location(cityId, address));
    }

    final public void setCuisine(final String cuisine) {
        this.cuisine = cuisine;
    }

    final public String getCuisine() {
        return this.cuisine;
    }

    final public void setURL(final String url) {
        this.url = url;
    }

    final public String getURL() {
        return url;
    }

    /*
        public void setAddress(final String address) {
            this.address = address

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
    */
    
}