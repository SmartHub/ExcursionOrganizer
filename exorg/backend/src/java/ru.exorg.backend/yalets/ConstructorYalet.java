package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.PoiShortForWeb;
import ru.exorg.backend.model.PoiTypeForWeb;
import ru.exorg.backend.model.RoutePointForWeb;
import ru.exorg.backend.services.PoiService;
import ru.exorg.backend.services.PoiTypeService;
import ru.exorg.core.model.POI;
import ru.exorg.core.model.PoiType;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:36:31
 * To change this template use File | Settings | File Templates.
 */
public class ConstructorYalet implements Yalet {
    private PoiTypeService poiTypeService;
    private PoiService poiService;
    private static Logger log = Logger.getLogger("Performance");

    @Required
    public void setPoiTypeService (final PoiTypeService pts) {
        this.poiTypeService = pts;
    }

    @Required
    public void setPoiService (final PoiService ps) {
        this.poiService = ps;
    }

    private void SetPoiTypes(InternalResponse res) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Started processing poi types"));

            List<PoiType> poiTypes = poiTypeService.getPoiTypes();
            for (PoiType t : poiTypes) {
                res.addWrapped("type", new PoiTypeForWeb(t.getName()));
            }
            
            long stop = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Stopped processing poi types. Time elapsed: %d ms", stop - start));
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private void SetRoutePoints(final InternalRequest req, InternalResponse res) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Started setting route points"));

            HttpSession s = req.getHttpServletRequest().getSession();
            List<RoutePointForWeb> rps = (List<RoutePointForWeb>)s.getAttribute("route");

            if (rps != null) {
                /*for (RoutePointForWeb p : rps) {
                    System.out.println("SetRoutePoints: " + p.getOrder() + " " + p.getName());
                }*/
                for (RoutePointForWeb r : rps) {
                    POI poi = poiService.getPoiById(r.getPoiId());
                    //res.addWrapped("poi", new PoiShortForWeb(poi));
                    res.addWrapped("route_point", new RoutePointForWeb(rps.indexOf(r), poi));
                }
                //res.addWrapped("route", rps);
            }
            else
                System.out.println("empty route");

            long stop = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Stopped setting route points. Time elapsed: %d ms", stop - start));
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }



    public void process(InternalRequest req, InternalResponse res) {

        /*Map<String, List<String>> m = req.getAllParameters();
        for (Map.Entry<String, List<String>> e : m.entrySet()) {
            System.out.println("Param: " + e.getKey() + ", value:" + e.getValue().get(0));
        }*/
        

        if (req.getParameter("poi_id") != null) {

            long start = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Started processing route point (poi %d)", req.getLongParameter("poi_id")));

            HttpSession s = req.getHttpServletRequest().getSession();
            List<RoutePointForWeb> rps = (List<RoutePointForWeb>)s.getAttribute("route");
            if (rps == null) {
                rps = new ArrayList<RoutePointForWeb>();
                s.setAttribute("route", rps);
            }
            POI poi = poiService.getPoiById(req.getLongParameter("poi_id"));
            if (req.getParameter("action") != null) {
                System.out.println("action : " + req.getParameter("action"));

                if (req.getParameter("action").toString().length()==8) { // delete
                    int idx = RoutePointForWeb.getListIndexOf(rps, poi.getName());
                    if (idx != -1) {
                        rps.remove(RoutePointForWeb.getListIndexOf(rps, poi.getName()));
                    }
                }
                else { // add
                    if (!RoutePointForWeb.existsInList(rps, poi.getName())) {
                        int order = (rps.size() != 0) ? rps.get(rps.size()-1).getOrder()+1 : 0;
                        rps.add(new RoutePointForWeb(order, poi));
                    }
                }
            }

            res.addWrapped("route", rps);
            SetRoutePoints(req, res);

            /*for (RoutePointForWeb p : rps) {
                System.out.println("Item: " + p.getOrder() + " " + p.getName());
            }*/

            long stop = System.currentTimeMillis();
            log.debug(String.format("ConstructorYalet : Stopped processing route point (poi %d). Time elapsed: %d ms", req.getLongParameter("poi_id"), stop - start));                      


        }
        else {
            SetPoiTypes(res);
            SetRoutePoints(req, res);
        }
    }
}