package eo.processing;

import java.util.*;
import org.springframework.beans.factory.InitializingBean;

import eo.common.POI;

// ================================================================================

public class Main implements InitializingBean{
    private POI poi_;

    public Main(POI poi) {
        poi_ = poi;
    }

    public void afterPropertiesSet() {
        Iterator it = poi_.poiIterator();
        while (it.hasNext()) {
            POI.Entry e = (POI.Entry)it.next();
            System.out.println(e.name());
        }
    }
}