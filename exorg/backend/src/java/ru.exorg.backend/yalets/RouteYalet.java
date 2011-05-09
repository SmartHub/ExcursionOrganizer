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
import ru.exorg.backend.services.PoiService;
import ru.exorg.core.model.POI;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

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
    private PoiService poiService;

    @Required
    public void setRouteService (final RouteService rs) {
        this.rs = rs;
    }

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    public void setPoiService(final PoiService ps) {
        this.poiService = ps;
    }

    private void SetRoutePoints (InternalRequest req, InternalResponse res, final int routeId, final String routeType) {
        if (routeType.equals("r"))
        {
            HttpSession s = req.getHttpServletRequest().getSession();
            List<Long> rps = new ArrayList<Long>();

            /*
            List<RoutePointForWeb> rps = (List<RoutePointForWeb>)s.getAttribute("route_points");
            if (rps == null) {
                rps = new ArrayList<RoutePointForWeb>();
                s.setAttribute("route_points", rps);
            }
            */

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
                    rps.add(p.getId());
                }

                s.setAttribute("route", rps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        /*
        HttpSession s = req.getHttpServletRequest().getSession();
        List<Long> rps = (List<Long>)s.getAttribute("route");
        for (Long id : rps) {
            System.out.println("Item: " + poiService.getPoiById(id).getName());
        }
        */

        // user route
        System.out.println("user route");
        
    }

    public void process (InternalRequest req, InternalResponse res) {
        SetRoutePoints(req, res, req.getIntParameter("id"), req.getParameter("type"));//req.getHttpServletRequest().getSession().getAttribute("route_type").toString());
    }
}
