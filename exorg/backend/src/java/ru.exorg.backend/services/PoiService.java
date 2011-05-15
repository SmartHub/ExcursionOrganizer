package ru.exorg.backend.services;

import org.apache.lucene.document.Document;

import java.util.ArrayList;
import java.util.List;

import ru.exorg.core.model.Description;
import ru.exorg.core.model.POI;
import ru.exorg.core.lucene.Search;
import ru.exorg.core.lucene.DocMapper;


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
    private Search searcher;

    private POIMapper poiMapper;

    private static final int Id = 1;
    private static final int Name = 2;
    private static final int Type = 3;
    private static final int Address = 4;
    private static final int Description = 5;
    private static final int Description_url = 6;
    private static final int Img_url = 7;
    private static final int Lat = 8;
    private static final int Lng = 9;
    private static final int Cluster_id = 10;
    private static final int Is_head = 11;
    private static final int Square_num = 12;

    final private static String POIFields =
            "id, name, type, address, descr, descr_ref, img_url, lat, lng, cluster_id, is_head, square_num";

    private static class POIMapper implements DocMapper<POI> {
        public POI mapDoc(Document doc) {
            POI poi = new POI(doc.get("id"), doc.get("name"));

            poi.setAddress(doc.get("address"));
            poi.setLocation(doc.get("lat"), doc.get("lng"));

            poi.setClusterId(doc.get("clusterId"));
            poi.setClusterHeadFlag(doc.get("isClusterHead"));
            poi.setSquareId(doc.get("squareId"));

            String s = doc.get("description");
            if (s != null) {
                poi.addDescription(s, doc.get("descriptionURL"));
            }

            s = doc.get("imageURL");
            if (s != null) {
                poi.addImage(s);
            } else {
                poi.addImage("");
            }

            return poi;
        }
    }

    public PoiService() {
        this.poiMapper = new POIMapper();
    }

    public void setSearcher(Search s) {
        this.searcher = s;
    }

    public List<POI> getPoiListByType(final String type) throws Exception {
        return this.searcher.search(String.format("type: %s", type), this.poiMapper);
    }

    public POI getPoiById(long poiId) {
        try {
            POI poi = getRawPoiById(poiId);

            if (poi == null) {
                System.out.println("Epic Fail: no POI :(");
            }

            if (poi.getId() != 0)
            {
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
            }
            return poi;

        } catch (Exception e) {
            System.out.println("Querying POI " + String.valueOf(poiId) + " failed");

            //e.printStackTrace();

            return null;
        }
    }

    public List<POI> getClusteredPoiList(long clusterId)
    {
        try {
            return this.searcher.search(String.format("clusterId: %d", clusterId), this.poiMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private POI getRawPoiById(long id) throws Exception {
        try {
            return this.searcher.search(String.format("id: %d", id), this.poiMapper).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int[] getNearestSquares(int square, int rows, int columns)
    {
        int[] squares = new int[8];
        int lastIndex = -1;
        if (square%columns != 0)
        {
            squares[++lastIndex] = square -1;
        }
        if ((square - columns) >= 0)
        {
            squares[++lastIndex] = square - columns;
            if (square%columns != columns-1)
            {
                squares[++lastIndex] = square - columns + 1;
            }
            if (square%columns != 0)
            {
                squares[++lastIndex] = square - columns -1;
            }

        }
        if ((square + columns) <= (columns-1)*(rows-1))
        {
            squares[++lastIndex] = square + columns;
            if (square%columns != columns-1)
            {
                squares[++lastIndex] = square + columns + 1;
            }
            if (square%columns != 0)
            {
                squares[++lastIndex] = square + columns -1;
            }
        }
        if (square%columns != (columns - 1))
        {
            squares[++lastIndex] = square + 1;
        }
        if (lastIndex < 7)
        {
            int[] sqs = new int[lastIndex+1];
            for (int i = 0; i <= lastIndex; ++i)
            {
                sqs[i] = squares[i];
            }
            return sqs;
        }
        return squares;

    }

    private List<POI> getPoisFromSquare(int square) throws Exception {
        return this.searcher.search(String.format("squareId: %d", square), this.poiMapper);
    }

    public List<POI> getNearestPois(long poiId) throws Exception {
        List<POI> pois = new ArrayList<POI>();

        POI poi = getRawPoiById(poiId);
        if (poi.getId() != 0)
        {
            int[] squares = getNearestSquares(poi.getSquareId(), 20, 20);      //hack, because we should take info about cols and rows from db or index
            for (int i = 0; i < squares.length -1; ++i)
            {
                pois.addAll(getPoisFromSquare(squares[i]));
            }
        }
        return pois;
    }


}
