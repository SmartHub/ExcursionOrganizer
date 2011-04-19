package ru.exorg.model;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 15.04.11
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public class RoutePoint {

    private POI poi;

    private int order;

    public RoutePoint(POI poi, int order ) {
        this.poi = poi;
        this.order = order;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
