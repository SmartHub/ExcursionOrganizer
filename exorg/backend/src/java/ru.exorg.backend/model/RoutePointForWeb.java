package ru.exorg.backend.model;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 29-Apr-2011
 * Time: 16:20:08
 * To change this template use File | Settings | File Templates.
 */

public class RoutePointForWeb {
    private int order;
    private long poiId;
    private double lat;
    private double lng;
    private String name;
    private String address;
    //private String image;

    public RoutePointForWeb (final int order, final String name, final String address, final long poiId, final double lat, final double lng) {
        this.order = order;
        this.name = name;
        this.address = address;
        this.poiId = poiId;
        this.lat = lat;
        this.lng = lng;
    }

    public int getOrder () {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public long getPoiId () {
        return poiId;
    }

    public double getLng () {
        return lng;
    }

    public double getLat () {
        return lat;
    }
}
