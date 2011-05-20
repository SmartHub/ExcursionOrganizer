package ru.exorg.backend.model;

import ru.exorg.core.model.POI;

import java.util.List;

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

    public RoutePointForWeb (final int order, final POI poi) {
        this.order = order;
        name = poi.getName();
        address = poi.getAddress();
        poiId = poi.getId();
        lat = poi.getLocation().getLat();
        lng = poi.getLocation().getLng();
    }

    public int getOrder () {
        return order;
    }

    public void setOrder (final int order) {
        this.order = order;
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

    public static int getListIndexOf(final List<RoutePointForWeb> list, final String name) {
        for (RoutePointForWeb r : list) {
            if (r.getName().equals(name)) {
                return list.indexOf(r);
            }
        }
        return -1;
    }

    public static boolean existsInList(final List<RoutePointForWeb> list, final String name) {
        for (RoutePointForWeb r : list) {
            if (r.getName().equals(name))
                return true;
        }
        return false;
    }

    public static void setOrder (List<RoutePointForWeb> list, final long poi_id, final int order) {
        for (RoutePointForWeb r : list) {
            if (r.getPoiId() == poi_id) {
                r.setOrder(order);
                return;
            }
        }
    }
}
