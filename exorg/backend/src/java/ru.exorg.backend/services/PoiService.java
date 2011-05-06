package ru.exorg.backend.services;

import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;
import ru.exorg.core.model.Description;
import ru.exorg.core.model.POI;
import ru.exorg.core.model.PoiType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 1:43
 * To change this template use File | Settings | File Templates.
 */
public class PoiService {

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
    private static final int Cluster_id = 9;
    private static final int Is_head = 10;
    private static final int Square_num = 11;
    private static final int SourceUrl = 13;

    public PoiService() {
        sphinx_host = "localhost";
        sphinx_port = 9312;
        this.sphinxClient = new SphinxClient(sphinx_host, sphinx_port);

        try {
            this.sphinxClient.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED);
        } catch (Exception e) {
            System.out.println("Sun has raised in the west today :(");
        }
    }

    private String returnValue(List<String> inf, int index)
    {
        String res = inf.get(index);
        if (res.equals(null))
        {
            return "";
        }else
        {
            return res;
        }

    }

    private POI getPOIFromMatch(SphinxMatch match)
    {
        ArrayList<String> inf = match.attrValues;

        long id = Long.parseLong(inf.get(Id));

        String name = returnValue(inf, Name); //inf.get(Name);

        POI poi = new POI(id, name);

        PoiTypeService typeService = new PoiTypeService();
        PoiType type = typeService.getPoiTypeByName(inf.get(Type));
        poi.setTypeObj(type);
        poi.setType(type.getId());

        poi.setAddress(returnValue(inf, Address));


        Double lat = Double.parseDouble(inf.get(Lat));

        Double lng = Double.parseDouble(inf.get(Lng));

        poi.setLocation(lat, lng);


        String descr = returnValue(inf,Description);

        String descrUrl = returnValue(inf,Description_url);

        if (!descr.equals("") && !descrUrl.equals(""))
        {
            poi.addDescription(descr, descrUrl);
        }

        if (!inf.get(Img_url).equals(null))
        {
            poi.addImage(inf.get(Img_url));

        }

        Long clId = Long.parseLong(inf.get(Cluster_id));

        poi.setClusterId(Long.parseLong(inf.get(Cluster_id)));



        if (Integer.parseInt(inf.get(Is_head)) == 0) {
            poi.setClusterHeadFlag(false);
        } else {
            poi.setClusterHeadFlag(true);
        }
        poi.setSquareId(Integer.parseInt(inf.get(Square_num)));
        //System.out.println("getPOIFromMatch: squareId " + String.valueOf(Integer.parseInt(inf.get(Square_num))));
        //System.out.println("getPOIFromMatch: URL " + returnValue(inf, SourceUrl));
        //poi.setURL(returnValue(inf, SourceUrl));
        //System.out.println("getPOIFromMatch: before return: " + poi.toString());
        return poi;
    }

    public List<POI> getPoiListByKey(String key) throws SphinxException {

        SphinxResult sphinxResult = sphinxClient.Query(key);
        List<POI> result = new ArrayList<POI>();
        for(SphinxMatch match: sphinxResult.matches)
        {
            result.add(getPOIFromMatch(match));
        }

        return result;
    }

    public List<POI> getPoiListByType(String type) throws SphinxException {

        List<POI> pois = new ArrayList<POI>();
        POI poi = null;
        SphinxResult result = sphinxClient.Query("@type " + type, "poi_index");
        for(SphinxMatch match: result.matches)
        {
            poi = getPOIFromMatch(match);
            poi = getPoiById(poi.getId());
            pois.add(poi);

        }
        return pois;
    }

    public POI getPoiById(long poiId)
    {
        POI poi = new POI();
        try {
            poi = getRawPoiById(poiId);


            List<POI> clusteredPois = getClusteredPoiList(poi.getClusterId());

            for(POI p: clusteredPois)
            {
              if (p.getId() != poi.getId()) {
                if ((poi.getLocation().getLat() < 0) && (p.getLocation().getLat() > 0))    //hasLocation ??
                {

                    poi.setLocation(p.getLocation().getLat(), p.getLocation().getLng());
                }
                if ((!poi.hasAddress()) && (p.hasAddress()))
                {

                    poi.setAddress(p.getAddress());
                }
                /*
                if ((poi.getURL().equals("")) && (!p.getURL().equals("")) )
                {
                    System.out.println("getPoiById: update URL " + p.getURL());
                    poi.setURL(p.getURL());
                }
                */
                if (p.getDescriptions().size() > 0)
                {
                    for (Description descr: p.getDescriptions())
                    {
                        poi.addDescription(descr);
                    }
                }
                if (p.getImages().size() > 0)
                {
                    for (String imageUrl: p.getImages())
                    {
                        poi.addImage(imageUrl);
                    }
                }
              }
            }
        } catch (SphinxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.out.println("getRawPoiById failed for id " + String.valueOf(poiId));
        }
        return poi;
    }

    public List<POI> getClusteredPoiList(long clusterId)
    {
        List<POI> pois = new ArrayList<POI>();

        try {
            SphinxResult result = sphinxClient.Query("@cluster_id " + clusterId, "poi_index");
            for(SphinxMatch match: result.matches)
            {
                pois.add(getPOIFromMatch(match));
            }
        } catch (SphinxException e) {
            System.out.println("There is no any poi in the index with cluster_id = " + String.valueOf(clusterId));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return pois;
    }

    private POI getRawPoiById(long id) throws SphinxException {

        SphinxResult result = sphinxClient.Query("@id " + String.valueOf(id), "poi_index");

        for(SphinxMatch match: result.matches)
        {
             POI poi = getPOIFromMatch(match);

             if (poi.getId() == id)
             {

                 return poi;
             }
        }
        return new POI();
    }


}
