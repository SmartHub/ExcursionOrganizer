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

    public PoiType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PoiType(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    // like Searcher.queryByType
    public int[] GetAllPoiTypes () {
        return null;
        
    }

}