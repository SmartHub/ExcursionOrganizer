package ru.exorg.model;

import java.lang.*;
import java.util.*;

// ================================================================================

public class Cafe {
    private long id;
    private String name;
    private long cityId;
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

    final public long getCityId() {
        return this.cityId;
    }

    final public void setCityId(long cityId) {
        this.cityId = cityId;
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

    final public void addLocation(final String address, double lat, double lng) {
        this.locations.add(new Location(this.cityId, address, lat, lng));
    }

    final public void addLocation(final String address) {
        this.locations.add(new Location(this.cityId, address));
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
}