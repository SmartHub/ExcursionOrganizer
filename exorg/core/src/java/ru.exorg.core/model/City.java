package ru.exorg.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 15.04.11
 * Time: 23:20
 * To change this template use File | Settings | File Templates.
 */
public class City {
    private long id;
    private String name;
    private LatLng neLatLng;
    private LatLng swLatLng;
    private int latSubdiv;
    private int lngSubdiv;

    public City() { }

    public City(long id, String name, LatLng neLatLng, LatLng swLatLng) {
        this.id = id;
        this.name = name;
        this.neLatLng = neLatLng;
        this.swLatLng = swLatLng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getNeLatLng() {
        return neLatLng;
    }

    public void setNeLatLng(LatLng neLatLng) {
        this.neLatLng = neLatLng;
    }

    public LatLng getSwLatLng() {
        return swLatLng;
    }

    public void setSwLatLng(LatLng swLatLng) {
        this.swLatLng = swLatLng;
    }

    final public int getLngSubdivs() {
        return this.lngSubdiv;
    }

    final public void setLngSubdivs(int subdiv) {
        this.lngSubdiv = subdiv;
    }

    final public int getLatSubdivs() {
        return this.latSubdiv;
    }

    final public void setLatSubdivs(int subdiv) {
        this.latSubdiv = subdiv;
    }

    final public double getLatSubdivLen() {
        return Math.abs(this.swLatLng.getLng() - this.neLatLng.getLng())/this.latSubdiv;
    }

    final public double getLngSubdivLen() {
        return Math.abs(this.swLatLng.getLat() - this.neLatLng.getLat())/this.lngSubdiv;
    }

    //poi.setAddress(poi.getAddress().replaceAll("^\\d+,\\s*", "").replaceAll("\\(.*?\\)", ""));
}
