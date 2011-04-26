package ru.exorg.backend.services;

import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;

import ru.exorg.core.model.POI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 1:43
 * To change this template use File | Settings | File Templates.
 */
public class POIService {

    private String sphinx_host;

    private int sphinx_port;

    private SphinxClient sphinxClient;

    private static final int Id = 0;
    private static final int Name = 1;
    private static final int Type = 2;
    private static final int Address = 3;
    private static final int Description = 4;
    private static final int Description_url = 5;
    private static final int Img_url = 6;
    private static final int Lat = 7;
    private static final int Lng = 8;

    public POIService() {
        sphinx_host = "localhost";
        sphinx_port = 9312;
        this.sphinxClient = new SphinxClient(sphinx_host, sphinx_port);

        try {
            this.sphinxClient.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED);
        } catch (Exception e) {
            System.out.println("Sun has raised in the west today :(");
        }
    }

    private POI getPOIFromMatch(SphinxMatch match)
    {
        ArrayList<String> inf = match.attrValues;

        long id = Long.parseLong(inf.get(Id));
        String name = inf.get(Name);
        POI poi = new POI(id, name);

        poi.setAddress(inf.get(Address));

        Double lat = Double.parseDouble(inf.get(Lat));
        Double lng = Double.parseDouble(inf.get(Lng));
        poi.setLocation(lat, lng);

        poi.addDescription(inf.get(Description), inf.get(Description_url));

        poi.addImage(inf.get(Img_url));

        //poi.setType();

        return poi;
    }

    public List<POI> getPoiListByKey(String key) throws SphinxException {
        //System.out.println ("getPoiListByKey: key = "+key);
        
        SphinxResult sphinxResult = sphinxClient.Query("@id "+key);
        List<POI> result = new ArrayList<POI>();
        for(SphinxMatch match: sphinxResult.matches)
        {
            result.add(getPOIFromMatch(match));
        }

        //System.out.println (result.size());
        return result;
    }

    public List<POI> getPoiListByType(String type) throws SphinxException {

        List<POI> result = getPoiListByKey(type);
        long type_id = 1; //
        for(POI poi: result)
        {
            if (poi.getType() != type_id)
            {
                result.remove(poi);
            }
        }
        return result;
    }

    public POI getPoiById(long id) throws SphinxException {

        List<POI> result = getPoiListByKey(String.valueOf(id));
        for(POI poi: result)
        {
            if (poi.getId() != id)
            {
                result.remove(poi);
            }
        }
        return result.get(0);
    }


}
