package eo.frontend.httpserver;

import java.lang.*;

import javax.servlet.http.HttpServletRequest;

// ================================================================================

public class TestPOI implements DynamicHandler {
    public Response handle(final HttpServletRequest request) {
        try {
            Response r = new Response();

            r.result = Searcher.queryById(1);
            r.aliases.put("poi", Searcher.POI.class);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}