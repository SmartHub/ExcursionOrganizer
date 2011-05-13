package ru.exorg.backend.model;

import ru.exorg.core.model.POI;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 13-May-2011
 * Time: 13:36:42
 * To change this template use File | Settings | File Templates.
 */
public class PoiNearestForWeb {
    private long id;
    private double lat;
    private double lng;
    private String name;
    private String address;

    public PoiNearestForWeb (final POI p) {
        id = p.getId();
        name = p.getName();
        address = p.getAddress();
        lat = p.getLocation().getLat();
        lng = p.getLocation().getLng();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getId () {
        return id;
    }

    public double getLng () {
        return lng;
    }

    public double getLat () {
        return lat;
    }
}
