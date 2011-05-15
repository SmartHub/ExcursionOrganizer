package ru.exorg.backend.services;

import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxMatch;
import org.sphx.api.SphinxResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.exorg.core.model.Description;
import ru.exorg.core.model.POI;
import ru.exorg.core.model.PoiType;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private JdbcTemplate poiIndex;
    private POIMapper poiMapper;

    /*
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
    */

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

    private static class POIMapper implements RowMapper<POI> {
        public POI mapRow(ResultSet rs, int rowNum) throws SQLException {
            POI poi = new POI(rs.getLong(Id), rs.getString(Name));

            poi.setAddress(rs.getString(Address));
            poi.setLocation(rs.getDouble(Lat), rs.getDouble(Lng));

            poi.setClusterId(rs.getLong(Cluster_id));
            poi.setClusterHeadFlag(rs.getBoolean(Is_head));
            poi.setSquareId(rs.getInt(Square_num));

            String s = rs.getString(Description);
            if (s != null) {
                poi.addDescription(s, rs.getString(Description_url));
            }

            s = rs.getString(Img_url);
            if (s != null) {
                poi.addImage(s);
            } else {
                poi.addImage("");
            }

            return poi;
        }
    }

    public PoiService() {
        /*
        sphinx_host = "localhost";
        sphinx_port = 9312;

        this.sphinxClient = new SphinxClient(sphinx_host, sphinx_port);

        try {
            this.sphinxClient.SetMatchMode(SphinxClient.SPH_MATCH_EXTENDED);
        } catch (Exception e) {
            System.out.println("Sun has raised in the west today :(");
        }
        */

        this.poiMapper = new POIMapper();
    }

    public void setPoiIndex(JdbcTemplate jdbcTemplate) {
        this.poiIndex = jdbcTemplate;
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

    /*
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
    */

    public List<POI> getPoiListByKey(String key) throws Exception {
        /*
        SphinxResult sphinxResult = sphinxClient.Query(key);
        List<POI> result = new ArrayList<POI>();
        for(SphinxMatch match: sphinxResult.matches)
        {
            result.add(getPOIFromMatch(match));
        }

        return result;
        */

        return null;
    }


    public List<POI> getPoiListByType(String type) throws Exception {
        /*List<POI> pois = new ArrayList<POI>();
        POI poi = null;
        SphinxResult result = sphinxClient.Query("@type " + type + "@is_head 1" , "poi_index");
        for(SphinxMatch match: result.matches)
        {
            poi = getPOIFromMatch(match);
            poi = getPoiById(poi.getId());
            System.out.println("getPoiListByType: poi" + poi.toString());
            System.out.println("getPoiListByType: id" + String.valueOf(poi.getId()));
            System.out.println("getPoiListByType: lat" + String.valueOf(poi.getLocation().getLat()));
            System.out.println("getPoiListByType: lng" + String.valueOf(poi.getLocation().getLng()));
            System.out.println("getPoiListByType: address" + poi.getAddress());
            System.out.println("getPoiListByType: cluster_id" + String.valueOf(poi.getClusterId()));
            System.out.println("getPoiListByType: square_id" + String.valueOf(poi.getSquareId()));
            System.out.println("getPoiListByType: image" + poi.getImage());
            System.out.println("getPoiListByType: name" + poi.getName());
            System.out.println("getPoiListByType: type" + poi.getTypeObj().getName());
            System.out.println("getPoiListByType: description_size" + String.valueOf(poi.getDescriptions().size()));
            //System.out.println("getPoiListByType: i" + String.valueOf(poi.getImage()));
            pois.add(poi);

        }*/

        try {
            return this.poiIndex.query(String.format("SELECT %s FROM poi_index WHERE MATCH('@type %s') LIMIT 1000", POIFields, type), this.poiMapper);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public POI getPoiById(long poiId) {
        try {
            POI poi = getRawPoiById(poiId);

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
            e.printStackTrace();

            return null;
        }
    }

    public List<POI> getClusteredPoiList(long clusterId)
    {
        /*
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
        */

        try {
            return this.poiIndex.query("SELECT * FROM poi_index WHERE cluster_id=?;", new Object[]{clusterId}, this.poiMapper);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private POI getRawPoiById(long id) throws Exception {
        try {
            return this.poiIndex.queryForObject("SELECT * FROM poi_index WHERE id=?;", this.poiMapper, new Object[]{id});
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        /*
        try{
           SphinxResult result = sphinxClient.Query("@id " + String.valueOf(id), "poi_index");

           for(SphinxMatch match: result.matches)
            {
                POI poi = getPOIFromMatch(match);

                if (poi.getId() == id)
                {
                    return poi;
                }
            }
        } catch (SphinxException e)
        {
             System.out.println("getRawPoiById: poi with id " + String.valueOf(id) + "is absent in the index");
        }
        return new POI();
        */
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
        /*
        List<POI> pois = new ArrayList<POI>();
        try{
            //sphinxClient.AddQuery("@square_num " + String.valueOf(square), "poi_index");

            SphinxResult result = sphinxClient.Query("@square_num " + String.valueOf(square), "poi_index");
            for (SphinxMatch match: result.matches)
            {
                pois.add(getPOIFromMatch(match));
            }

        } catch (SphinxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.out.println("getPoisFromSquare: failed");
        }
        return  pois;
        */

        try {
            return this.poiIndex.query("SELECT * FROM poi_index WHERE square_num=?;", new Object[]{square}, this.poiMapper);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
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
