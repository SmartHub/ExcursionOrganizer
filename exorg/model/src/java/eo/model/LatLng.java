package eo.model;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 15.04.11
 * Time: 23:18
 * To change this template use File | Settings | File Templates.
 */
public class LatLng {

    private double lat;

    private double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

}
