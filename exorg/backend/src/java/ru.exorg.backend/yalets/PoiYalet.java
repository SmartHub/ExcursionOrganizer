package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.PoiForWeb;
import ru.exorg.backend.model.PoiNearestForWeb;
import ru.exorg.backend.services.PoiService;
import ru.exorg.core.model.POI;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 05-May-2011
 * Time: 02:17:30
 * To change this template use File | Settings | File Templates.
 */
public class PoiYalet implements Yalet {

    private PoiService poiService;
    private static Logger log = Logger.getLogger("Performance");

    @Required
    public void setPoiService(final PoiService ps) {
        poiService = ps;
    }

    private void SetPoiData (InternalResponse res, final int poiId) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("PoiYalet : Started processing poi %d", poiId));

            POI p = poiService.getPoiById(poiId);
            res.addWrapped("poi", new PoiForWeb(p));

            List<POI> nearestPois = poiService.getNearestPois(poiId);
            for (POI poi : nearestPois) {
                res.addWrapped("nearest_poi", new PoiNearestForWeb(poi));
            }

            long stop = System.currentTimeMillis();
            log.debug(String.format("PoiYalet : Stopped processing poi %d. Time elapsed: %d ms", poiId, stop - start));
        }
        catch (Exception e) {
            log.error(e);               
            e.printStackTrace();
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        SetPoiData(res, req.getIntParameter("id"));
    }
}
