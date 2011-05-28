package ru.exorg.backend.model;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:34:12
 * To change this template use File | Settings | File Templates.
 */
public class PoiTypeForWeb {
    private String name;
    private String icon;

    public PoiTypeForWeb(final String name, final String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName () {
        return name;
    }
    public String getIcon () {
        return icon;
    }
}
