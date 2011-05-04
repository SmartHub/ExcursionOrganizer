package ru.exorg.backend.yalets;

import net.sf.xfresh.core.InternalRequest;
import net.sf.xfresh.core.InternalResponse;
import net.sf.xfresh.core.Yalet;
import org.springframework.beans.factory.annotation.Required;
import ru.exorg.backend.model.PoiTypeForWeb;
import ru.exorg.backend.services.PoiTypeService;
import ru.exorg.core.model.PoiType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:36:31
 * To change this template use File | Settings | File Templates.
 */
public class ConstructorYalet implements Yalet {
    private PoiTypeService poiTypeService;

    @Required
    public void setPoiTypeService (final PoiTypeService pts) {
        this.poiTypeService = pts;
    }

    private void SetData (InternalResponse res) {
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
        SetData(res);
    }
}
