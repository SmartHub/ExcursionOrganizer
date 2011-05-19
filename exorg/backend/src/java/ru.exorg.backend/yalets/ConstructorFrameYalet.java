package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.PoiShortForWeb;
import ru.exorg.backend.services.PoiService;
import ru.exorg.core.model.POI;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 05-May-2011
 * Time: 02:00:04
 * To change this template use File | Settings | File Templates.
 */
public class ConstructorFrameYalet implements Yalet {
    private PoiService poiService;
    private static Logger log = Logger.getLogger("Performance");

    @Required
    public void setPoiService (final PoiService ps) {
        this.poiService = ps;
    }

    private void SetPois (InternalResponse res, final String type) {
        try {
            long start = System.currentTimeMillis();
            log.debug(String.format("ConstructorFrameYalet : Started processing pois by type %s", type));

            System.out.println(type);
            List<POI> pois = poiService.getPoiListByType(type);
            System.out.println(pois.size());

            for (POI poi: pois) {
                res.addWrapped("poi", new PoiShortForWeb(poi));
            }

            long stop = System.currentTimeMillis();
            log.debug(String.format("ConstructorFrameYalet : Stopped processing pois by type %s. Time elapsed: %d ms", type, stop - start));
        }
        catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }
    public void process(InternalRequest req, InternalResponse res) {
        
        SetPois(res, req.getParameter("name"));
    }
}
