package eo.frontend.httpserver;

import java.lang.*;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

// ================================================================================

public class TestPOI implements DynamicHandler {
    public Response handle(final Request request) {
        try {
            Response r = new Response();

            if (request.getParameterValues("id") != null) {
                int poi_id = Integer.parseInt((String)request.getParameterValues("id")[0]);

                r.result = Searcher.queryById(poi_id);
                r.aliases.put("poi", Searcher.POI.class);
            } else {
                System.out.println("Nothing to answer :(");
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}