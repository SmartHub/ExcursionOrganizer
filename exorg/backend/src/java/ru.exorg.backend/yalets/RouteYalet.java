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

    private void SetRoutePoints (InternalRequest req, InternalResponse res) {
        int routeId  = 0;
        if (req.getAllParameters().containsKey("id")) {
            routeId = req.getIntParameter("id");
        }

        final String routeType = req.getParameter("type");

        if (routeType != null)
        {
            HttpSession s = req.getHttpServletRequest().getSession();
            List<Long> rps = new ArrayList<Long>();

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
        } else {
            HttpSession s = req.getHttpServletRequest().getSession();
            List<Long> rps = (List<Long>)s.getAttribute("route");

            for (int i = 0; i < rps.size(); ++i) {
                POI p = poiService.getPoiById(rps.get(i));

                res.addWrapped("route_point", new RoutePointForWeb(i,
                                                                   p.getName(),
                                                                   p.getAddress(),
                                                                   p.getId(),
                                                                   p.getLocation().getLat(),
                                                                   p.getLocation().getLng()));
            }
        }
    }

    public void process (InternalRequest req, InternalResponse res) {
        SetRoutePoints(req, res);
    }
}
