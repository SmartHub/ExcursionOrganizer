package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.eclipse.jetty.server.SessionManager;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.RecommendedRouteForWeb;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.services.RecommendedRouteService;

import java.util.List;

// ================================================================================

public class RecommendedRoutesYalet implements Yalet {
    private RecommendedRouteService rrs;

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    private void SetRecRouteList (InternalResponse res) {
        try {
            List<Route> rrlist = rrs.getRecommendedRouteList();
            for (Route r : rrlist) {

                //System.out.println("SetRecRouteList"+r.getId());
                res.addWrapped("rec_routes", new RecommendedRouteForWeb(r.getId(), r.getDescription(), r.getImage()));
            }
        }
        catch( Exception e ) {
            System.out.println("Set Recommended Route List Exception caught: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        SetRecRouteList(res);
    }
}
