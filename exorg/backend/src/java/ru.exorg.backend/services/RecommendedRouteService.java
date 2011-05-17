package ru.exorg.backend.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.lucene.document.Document;

import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RoutePoint;

import ru.exorg.core.lucene.*;


/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 3:37
 * To change this template use File | Settings | File Templates.
 */

public class RecommendedRouteService {
    private Search searcher;
    private PoiService poiService;
    private JdbcTemplate jdbcTemplate;
    private RRMapper rrMapper;

    private class RRMapper implements DocMapper<Route> {
        public Route mapDoc(Document doc) {
            List<RoutePoint> poiList = new ArrayList<RoutePoint>();
            String[] pois_raw = doc.get("poiList").split(" ");

            for (int i = 0; i < pois_raw.length; i++) {
                long poiId = Long.parseLong(pois_raw[i]);
                poiList.add(new RoutePoint(poiService.getPoiById(Long.parseLong(poiId), i + 1)));

            }

            Route route = new Route(doc.get("id"), doc.get("description"));
            route.setPoints(poiList);

            return route;
        }
    }

    public RecommendedRouteService() {
        rrMapper = new RRMapper();
    }

    public void setSearcher(Search s) {
        this.searcher = s;
    }

    public void setJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }

    public void setPoiService(PoiService ps) {
        this.poiService = ps;
    }

    public Route getRecommendedRoute(final long id) throws Exception
    {
        return this.searcher.search(String.format("DocType:\"%s\" AND id:%d", Indexer.DocTypeReadyRoute, id), rrMapper).get(0);
        /*
        String q = String.format(
                "SELECT descr, count_point, duration FROM route_recommended WHERE id = %d;",
                id
        );
        RowMapper<Route> mapper = new RowMapper<Route>() {
            public Route mapRow(ResultSet rs, int i) throws SQLException {
                Route route = new Route(id, rs.getString("descr"), rs.getInt("count_point"), rs.getDouble("duration"));
                return route;
            }
        };

        List<Route> list = jdbcTemplate.query(q, mapper);
        if (!list.isEmpty())
        {
            String query = String.format(
                    "SELECT poi_id, order_num FROM route_poi WHERE route_id = %d;",
                    list.get(0).getId()
            );

            RowMapper<RoutePoint> pointMapper = new RowMapper<RoutePoint>() {
                public RoutePoint mapRow(ResultSet rs, int i) throws SQLException {
                    RoutePoint routePoint = null;

                    try{
                        //System.out.println("extract poi "+rs.getInt("poi_id")+", order num "+rs.getInt("order_num"));

                        routePoint = new RoutePoint(poiService.getPoiById(rs.getLong("poi_id")), rs.getInt("order_num"));

                        //System.out.println("extract poi SphinxException");
                        //println(e.getMessage());
                    }
                    catch (Exception e)
                    {

                        //NOTE: we catch this when poi to extract doesn't exist

                        //System.out.println("extract poi IndexOutOfBoundsException");
                        System.out.println(e.getMessage());
                    }
                    return routePoint;
                }
            };
            try {
                list.get(0).setPoints(jdbcTemplate.query(query, pointMapper));
                //System.out.println("list.get(0).points size = " + list.get(0).getCountPoints());
            }
            catch (Exception e) {
                System.out.println("setPoints exception");
                e.printStackTrace();
            }
        }
        else
            System.out.println ("FUCK");
        return list.get(0);
        */
    }

    public List<Route> getRecommendedRouteList()  throws Exception
    {
        return this.searcher.search(String.format("DocType:\"%s\"", Indexer.DocTypeReadyRoute), rrMapper);
        /*
        String q = new String("SELECT id FROM route_recommended;");
        List<Integer> routeIds = jdbcTemplate.queryForList(q, Integer.class);
        if(!routeIds.isEmpty())
        {
            List<Route> result = new ArrayList<Route>();
            for(int i: routeIds)
            {
                result.add(getRecommendedRoute(i));
            }
            return result;
        }
        return null;
        */
    }
}