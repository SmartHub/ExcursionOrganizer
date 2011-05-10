package ru.exorg.backend.model;

import ru.exorg.core.model.POI;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:24:40
 * To change this template use File | Settings | File Templates.
 */
public class PoiShortForWeb {
    private long id;
    private String name;
    private String imgUrl;
    private double lat;
    private double lng;

    public PoiShortForWeb(final POI poi) {
        id = poi.getId();
        name = poi.getName();
        imgUrl = poi.getImage();
        lat = poi.getLocation().getLat();
        lng = poi.getLocation().getLng();
    }

    public long getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public String getImgUrl () {
        return imgUrl;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
}
