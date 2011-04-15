package eo.model;

// ================================================================================

final public class Location {
    private long cityId;
    private String address;
    private double lat;
    private double lng;

    public Location() { 
        this.address = null;
        this.lat = -1;
        this.lng = -1;
    }

    public Location(final String address) {
        this.address = address;
        this.lat = -1;
        this.lng = -1;
    }

    public Location(long cityId, final String address) {
        this.cityId = cityId;
        this.address = address;
        this.lat = -1;
        this.lng = -1;
    }

    public Location(long cityId, final String address, double lat, double lng) {
        this.cityId = cityId;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    final public String getAddress() {
        return this.address;
    }

    final public void setAddress(final String address) {
        this.address = address;
    }

    final public boolean hasAddress() {
        return address != null;
    }

    final public double getLat() {
        return lat;
    }

    final public void setLat(double lat) {
        this.lat = lat;
    }

    final public double getLng() {
        return lng;
    }

    final public void setLng(double lng) {
        this.lng = lng;
    }

    final public long getCityId() {
        return this.cityId;
    }

    final public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    final public boolean isValid() {
        return this.lat != -1 && this.lng != -1;
    }
}