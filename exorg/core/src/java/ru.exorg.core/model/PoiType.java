package ru.exorg.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 16-Apr-2011
 * Time: 01:27:45
 * To change this template use File | Settings | File Templates.
 */
public class PoiType {
    private long id;
    private String name;
    private String icon;

    private void init(long id, final String name, final String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public PoiType(long id, final String name, final String icon) {
        this.init(id, name, icon);
    }

    public PoiType(final String id, final String name, final String icon) {
        this.init(Long.valueOf(id), name, icon);
    }

    public PoiType(final String name, final String icon) {
        this.name = name;
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
