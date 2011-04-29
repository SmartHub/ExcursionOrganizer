package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RoutePoint;
import ru.exorg.backend.model.RoutePointForWeb;
import ru.exorg.backend.services.RecommendedRouteService;
import ru.exorg.backend.services.RouteService;
import ru.exorg.core.model.POI;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 29-Apr-2011
 * Time: 15:50:13
 * To change this template use File | Settings | File Templates.
 */
public class RouteYalet implements Yalet {
    private RouteService rs;
    private RecommendedRouteService rrs;

    @Required
    public void setRouteService (final RouteService rs) {
        this.rs = rs;
    }

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    private void SetRoutePoints (InternalResponse res, final int routeId, final String routeType) {
        if (routeType.equals("r"))
        {
            try {
                Route r = rrs.getRecommendedRoute(routeId);
                for (RoutePoint rp : r.getPoints()) {
                    POI p = rp.getPoi();
                    if (p.getLocation().getLat() == -1 || p.getLocation().getLng() == -1) continue;
                    res.addWrapped("route_point", new RoutePointForWeb(rp.getOrder(),
                                                                        p.getName(),
                                                                        p.getAddress(),
                                                                        p.getId(),
                                                                        p.getLocation().getLat(),
                                                                        p.getLocation().getLng()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        // user route
        System.out.println("user route");
        
    }

    public void process (InternalRequest req, InternalResponse res) {
        SetRoutePoints(res, req.getIntParameter("id"), req.getParameter("type"));//req.getHttpServletRequest().getSession().getAttribute("route_type").toString());
    }
}
