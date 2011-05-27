package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.SessionManager;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.RecommendedRouteForWeb;
import ru.exorg.backend.model.Route;
import ru.exorg.backend.services.RecommendedRouteService;

import java.util.List;

// ================================================================================

public class RecommendedRoutesYalet implements Yalet {
    private RecommendedRouteService rrs;
    private static Logger log = Logger.getLogger("Performance");

    @Required
    public void setRecommendedRouteService (final RecommendedRouteService rrs) {
        this.rrs = rrs;
    }

    private void SetRecRouteList (InternalResponse res) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("RecommendedRoutesYalet : Started processing recommended routes list"));

            List<Route> rrlist = rrs.getRecommendedRouteList();
            for (Route r : rrlist) {

                //System.out.println("SetRecRouteList"+r.getId());
                res.addWrapped("rec_routes", new RecommendedRouteForWeb(r.getId(), r.getName(), r.getDescription(), r.getImage()));
            }
            long stop = System.currentTimeMillis();
            log.debug(String.format("RecommendedRoutesYalet : Stopped processing recommended routes list. Time elapsed: %d ms", stop - start));

        }
        catch( Exception e ) {
            log.error(e);
            System.out.println("Set Recommended Route List Exception caught: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        SetRecRouteList(res);
    }
}
