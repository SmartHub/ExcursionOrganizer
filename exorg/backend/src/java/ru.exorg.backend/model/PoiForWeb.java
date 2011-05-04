package ru.exorg.backend.model;

import ru.exorg.core.model.POI;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 05-May-2011
 * Time: 02:14:40
 * To change this template use File | Settings | File Templates.
 */
public class PoiForWeb {
    private long id;
    private String name;
    private String imgUrl;
    private String url;
    private String descriptionUrl;
    private String description;
    private String address;
    private double lat;
    private double lng;

    public PoiForWeb (final POI p) {
        id = p.getId();
        name = p.getName();
        imgUrl = p.getImage();
        url = p.getURL();
        description = p.getDescriptions().get(0).getText();
        descriptionUrl = p.getDescriptions().get(0).getSourceURL();
        address = p.getAddress();
        lat = p.getLocation().getLat();
        lng = p.getLocation().getLng();
    }

    /*public PoiForWeb(final long id, final String name, final String imgUrl, final String srcUrl, final String description, final String address, final double lat, final double lng) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.srcUrl = srcUrl;
        this.description = description;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    } */

    public long getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public String getImgUrl () {
        return imgUrl;
    }
    public String getUrl () {
        return url;
    }
    public String getDescription () {
        return description;
    }
    public String getDescriptionUrl () {
        return descriptionUrl;
    }
    public String getAddress () {
        return address;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
}
