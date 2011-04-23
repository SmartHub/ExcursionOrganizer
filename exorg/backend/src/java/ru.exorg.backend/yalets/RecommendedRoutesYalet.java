package ru.exorg.backend.yalets;

import net.sf.xfresh.core.*;
import org.eclipse.jetty.server.*;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RecommendedRouteForWeb;
import ru.exorg.backend.services.RecommendedRouteService;

import java.util.List;

// ================================================================================

public class RecommendedRoutesYalet implements Yalet {
    //private SessionManager sm;
    private RecommendedRouteService rrs;

    /*final public void setSessionManager(SessionManager sm) {
        this.sm = sm;
    }*/

    /*public static class Test {
        public String s;

        final public String getSid() {
            return s;
        }
    }*/

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    private void SetRecRouteList (final InternalResponse res) {
        try {
            List<Route> rrlist = rrs.getRecommendedRouteList();
            for (Route r : rrlist) {
                //truly, image = r.getPoints().get(0).getPoi().getImages().get(0);
                res.add(new RecommendedRouteForWeb(r.getId(), r.getDescription(), "http://www.snap2objects.com/wp-content/uploads/2009/02/header.jpg"));
            }
            System.out.println("SetRecRouteList");
        }
        catch( Exception e ) {
            System.out.println("Set Recommended Route List Exception caught: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        SetRecRouteList(res);
        //Test t = new Test();
        //t.s = req.getHttpServletRequest().getSession().getId();

        //res.add(t);
    }
}
