package ru.exorg.backend.model;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 24-Apr-2011
 * Time: 00:20:23
 * To change this template use File | Settings | File Templates.
 */
public class RecommendedRouteForWeb {
    private long rrid;
    private String description;
    private String img;

    public RecommendedRouteForWeb (final long id, final String description, final String imgUrl) {
        this.rrid = id;
        this.description = description;
        this.img = imgUrl;
    }

    public void setId(final long id) {
        this.rrid = id;
    }
    public void setDescription (final String description) {
        this.description = description;
    }
    public void setImg (final String imgUrl) {
        this.img = imgUrl;
    }
    public long getId() {
        return rrid;
    }
    public String getDescription () {
        return description;
    }
    public String getImg () {
        return img;
    }
}
