package ru.exorg.backend.services;

import org.sphx.api.SphinxException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RoutePoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 3:37
 * To change this template use File | Settings | File Templates.
 */
public class RecommendedRouteService {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Route getRecommendedRoute(final long id) throws Exception
    {
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
             //System.out.println ("getting route " + list.get(0).getId());

             String query = String.format(
                     "SELECT poi_id, order_num FROM route_poi WHERE route_id = %d;",
                     list.get(0).getId()
             );

             RowMapper<RoutePoint> pointMapper = new RowMapper<RoutePoint>() {
                 public RoutePoint mapRow(ResultSet rs, int i) throws SQLException {
                     PoiService searcher = new PoiService();
                     RoutePoint routePoint = null;
                     try
                     {
                         //System.out.println("extract poi "+rs.getInt("poi_id")+", order num "+rs.getInt("order_num"));
                         routePoint = new RoutePoint(searcher.getPoiById(rs.getInt("poi_id")), rs.getInt("order_num"));
                     }

                     catch (SphinxException e) {
                         System.out.println("extract poi SphinxException");
                         System.out.println(e.getMessage());
                     }
                     catch (IndexOutOfBoundsException e)
                     {
                         //NOTE: we catch this when poi to extract doesn't exist

                         //System.out.println("extract poi IndexOutOfBoundsException");
                         //println(e.getMessage());
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
    }

    public List<Route> getRecommendedRouteList()  throws Exception
    {
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
    }
}
