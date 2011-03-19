package eo.frontend.httpserver;

import java.lang.*;

import javax.servlet.http.HttpServletRequest;

// ================================================================================

public class TestHandler implements DynamicHandler {
    private static class POI {
        private String name;
        private String description;
        private String location;

        public POI(String n, String d, String l) {
            name = n;
            description = d;
            location = l;
        }
    }

    public Response handle(final HttpServletRequest request) {
        Response r = new DynamicHandler.Response();

        POI[] ps = new POI[3];
        ps[0] = new POI("poi1", "poi1 description", "10 10");
        ps[1] = new POI("poi2", "poi2 description", "40 11");
        ps[2] = new POI("poi3", "poi3 description", "60 12");
            
        r.result = ps;
        r.aliases.put("poi", POI.class);
        r.aliases.put("poi-list", POI[].class);

        return r;
    }
}