package ru.exorg.backend.model;

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

    public PoiShortForWeb(final long id, final String name, final String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
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
}
