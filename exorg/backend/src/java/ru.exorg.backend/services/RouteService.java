package ru.exorg.backend.services;

import org.sphx.api.SphinxException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RoutePoint;
import ru.exorg.core.model.POI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 3:18
 * To change this template use File | Settings | File Templates.
 */
public class RouteService {

    private JdbcTemplate jdbcTemplate;

    private POIService poiService;

    public static class OrderPoiComparator implements Comparator<RoutePoint>
    {
        public int compare(RoutePoint point1, RoutePoint point2)
        {
            return point1.getOrder() - point2.getOrder();
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Required
    public void setPoiService (final POIService poiService) {
        this.poiService = poiService;
    }

    public Route getUserRoute(final long sid)
    {
        String q = String.format(
                                 "SELECT poi_id, ord_num FROM user_route WHERE sid = %d;",
                                       sid
                                 );
        RowMapper<RoutePoint> mapper = new RowMapper<RoutePoint>() {
            public RoutePoint mapRow(ResultSet rs, int i) throws SQLException {

                Long poi_id = rs.getLong("poi_id");

                RoutePoint routePoint = null;
                try {
                    POI poi = poiService.getPoiById(poi_id);
                    routePoint = new RoutePoint(poi, rs.getInt("ord_num"));

                } catch (SphinxException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return routePoint; // hack ??
            }
        };
        List<RoutePoint> listPoints = jdbcTemplate.query(q, mapper);

        Route route = new Route(sid, "user route", listPoints.size(), 0, listPoints);

        return route;
    }



    public Route addPointInUserRoute(long userId, long pointId)
    {
        Route route = null;
        try {
            route = getUserRoute(userId);
            POI newPoi = poiService.getPoiById(pointId);
            if (newPoi != null)
            {
                route.addPoint(new RoutePoint(newPoi, route.getCountPoints()));
                String query = String.format("INSERT INTO user_route(sid, poi_id, ord_num)" +
                        "VALUES (%d, %d, %d);", userId, pointId, route.getCountPoints() - 1);
                jdbcTemplate.execute(query);
            }
        } catch (SphinxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return route;

    }

    public Route deletePointFromUserRoute(long userId, long pointId)
    {
        Route route = null;
        try{
            route = getUserRoute(userId);
            POI delPoi = poiService.getPoiById(pointId);
            if (delPoi != null) {
                route.deletePoint(delPoi);
                String query = String.format("DELETE FROM user_route WHERE sid = %d AND poi_id = %d;",
                        userId, pointId);
            }

        }
        catch (SphinxException e) {
            e.printStackTrace();
        }
        return route;

    }


public Route setOrders(Route route)
{
    Collections.sort(route.getPoints(), new OrderPoiComparator());
    int i = 0;
    for(RoutePoint point: route.getPoints())
    {
        //deletePointFromUserRoute(route.getId(), point.getPoi().getId());
        point.setOrder(i);
        ++i;
        //addPointInUserRoute(route.getId(), point.getPoi().getId());
    }
    return  route;
}

}