package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.apache.log4j.Logger;
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
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 29-Apr-2011
 * Time: 15:50:13
 * To change this template use File | Settings | File Templates.
 */
public class RouteYalet implements Yalet {
    private RecommendedRouteService rrs;
    private static Logger log = Logger.getLogger("Performance");

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    private void SetResponseRoutePoints (final InternalRequest req, InternalResponse res) {
        long start = System.currentTimeMillis();
        log.debug(String.format("RouteYalet : Started setting route points"));

        HttpSession s = req.getHttpServletRequest().getSession();
        List<RoutePointForWeb> rps = (List<RoutePointForWeb>)s.getAttribute("route");

        for (RoutePointForWeb rp : rps) {
            res.addWrapped("route_point", rp);
            //System.out.println("Item: " + rp.getOrder() + " " + rp.getName());
        }

        long stop = System.currentTimeMillis();
        log.debug(String.format("RouteYalet : Stopped setting route points. Time elapsed: %d ms", stop - start));
    }

    private void SetRouteByRecommendedRoute (InternalRequest req) {
        long start = System.currentTimeMillis();
        log.debug(String.format("RouteYalet : Started setting route by recommended route"));

        HttpSession s = req.getHttpServletRequest().getSession();
        List<RoutePointForWeb> rps = new ArrayList<RoutePointForWeb>();
        try {
            int routeId = req.getIntParameter("id");
            Route r = rrs.getRecommendedRoute(routeId);
            for (RoutePoint rp : r.getPoints()) {
                POI p = rp.getPoi();
                if (p.getLocation().getLat() == -1 || p.getLocation().getLng() == -1)
                    continue;

                rps.add(new RoutePointForWeb(rp));
            }
            s.setAttribute("route", rps);
            long stop = System.currentTimeMillis();
            log.debug(String.format("RouteYalet : Stopped setting route by recommended route. Time elapsed: %d ms", stop - start));
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private void ChangeOrder (InternalRequest req) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("RouteYalet : Started changing order of route points"));

            HttpSession s = req.getHttpServletRequest().getSession();
            List<RoutePointForWeb> rps = (List<RoutePointForWeb>)s.getAttribute("route");

            Map<String, List<String>> m = req.getAllParameters();
            for (Map.Entry<String, List<String>> e : m.entrySet()) {
                try {
                    long poi_id = Long.parseLong(e.getKey());
                    int order = Integer.parseInt(e.getValue().get(0));
                    RoutePointForWeb.setOrder(rps, poi_id, order);
                }
                catch (NumberFormatException ex) {
                    //System.out.println("NumberFormatException");
                    continue;
                }
            }
            s.setAttribute("route", rps);

            long stop = System.currentTimeMillis();
            log.debug(String.format("RouteYalet : Stopped changing order of route points. Time elapsed: %d ms", stop - start));
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    public void process (InternalRequest req, InternalResponse res) {
        if (req.getParameter("change_order") != null) {
            //System.out.println("change order");
            ChangeOrder(req);
        }
        if (req.getParameter("type") != null) {
            SetRouteByRecommendedRoute(req);
        }
        SetResponseRoutePoints(req, res);
    }
}
