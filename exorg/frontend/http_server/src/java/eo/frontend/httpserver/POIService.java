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

public class POIService extends AbstractHandler {
    private static void writeResponse(final String content, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(content.getBytes().length);

        PrintWriter pw = response.getWriter();
        pw.print(content);
        pw.flush();
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException {

        try {
            if (target.substring(target.lastIndexOf("/") + 1).equals("poi")) {
                XStream xs = new XStream();
                String xml;

                if (baseRequest.getParameterValues("id") != null) {
                    int poi_id = Integer.parseInt(baseRequest.getParameterValues("id")[0]);
                    Searcher.POI poi = Searcher.queryById(poi_id);

                    xs.alias("poi", Searcher.POI.class);
                    xml = xs.toXML(poi);
                    
                    writeResponse(xml, response);
                } else if (baseRequest.getParameterValues("type") != null) {
                    String poi_type = baseRequest.getParameterValues("type")[0];

                    int[] poi_ids = Searcher.queryByType(poi_type);

                    xs.alias("pois", int[].class);
                    xs.alias("id", int.class);
                    xml = xs.toXML(poi_ids);

                    writeResponse(xml, response);
                } else {
                    String[] types = Searcher.queryTypes();

                    xs.alias("poi-types", String[].class);
                    xs.alias("type", String.class);
                    xml = xs.toXML(types);

                    writeResponse(xml, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}