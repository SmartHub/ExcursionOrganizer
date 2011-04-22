package ru.exorg.core.model;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 16-Apr-2011
 * Time: 01:27:45
 * To change this template use File | Settings | File Templates.
 */
public class POIType {
    private long id;
    private String name;
    private String guess_rx;

    public POIType (long id, String name, String guess_rx) {
        this.id = id;
        this.name = name;
        this.guess_rx = guess_rx;
    }

    // like Searcher.queryByType
    public int[] GetAllPoiTypes () {
        return null;
        
    }

}
