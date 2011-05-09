package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.PoiShortForWeb;
import ru.exorg.backend.model.PoiTypeForWeb;
import ru.exorg.backend.services.PoiTypeService;
import ru.exorg.backend.services.PoiService;
import ru.exorg.core.model.PoiType;
import ru.exorg.core.model.POI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

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

    @Required
    public void setPoiTypeService (final PoiTypeService pts) {
        this.poiTypeService = pts;
    }

    public void setPoiService (final PoiService ps) {
        this.poiService = ps;
    }

    private void SetData(InternalResponse res) {
        try {
            List<PoiType> poiTypes = poiTypeService.getPoiTypes();
            for (PoiType t : poiTypes) {
                res.addWrapped("type", new PoiTypeForWeb(t.getName()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(InternalRequest req, InternalResponse res) {
        /*
        Map<String, List<String>> m = req.getAllParameters();
        for (Map.Entry<String, List<String>> e : m.entrySet()) {
            System.out.println("Param: " + e.getKey() + ", value:" + e.getValue().get(0));
        }
        */

        if (req.getParameter("poi_id") != null) {
            HttpSession s = req.getHttpServletRequest().getSession();
            List<Long> rps = (List<Long>)s.getAttribute("route");
            if (rps == null) {
                rps = new ArrayList<Long>();
                s.setAttribute("route", rps);
            }

            POI poi = poiService.getPoiById(req.getLongParameter("poi_id"));
            res.addWrapped("poi", poi);
            rps.add(poi.getId());

            for (Long id : rps) {
                System.out.println("Item: " + poiService.getPoiById(id).getName());
            }
        } else {
            SetData(res);
        }
    }
}
