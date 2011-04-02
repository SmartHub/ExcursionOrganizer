package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

import com.thoughtworks.xstream.XStream;

// ================================================================================

public class POITypeHandler implements DynamicHandler {
    public Response handle(final Request request) {
        try {
            Response r = new Response();
            String type_name = request.getParameterValues("name")[0];

            if (type_name != null) {
                int[] poi_ids = Searcher.queryByType(type_name);
                Searcher.POI[] pois = new Searcher.POI[poi_ids.length];

                System.out.println(pois.length);

                for (int i = 0; i < poi_ids.length; ++i) {
                    pois[i] = Searcher.queryById(poi_ids[i]);
                }

                r.result = pois;
            }

            r.aliases.put("poi", Searcher.POI.class);
            r.aliases.put("pois", Searcher.POI[].class);

            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}
