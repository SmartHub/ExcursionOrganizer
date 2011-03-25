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

public class RouteService extends AbstractHandler {
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException {
        try {
            if (target.substring(target.lastIndexOf("/") + 1).equals("route")) {
                int route_id = Integer.parseInt(baseRequest.getParameterValues("id")[0]);
                Searcher.Route route = Searcher.queryRoute(route_id);

                XStream xs = new XStream();
                xs.alias("route", Searcher.Route.class);
                xs.alias("poi", int.class);
                //xs.alias("item", Searcher.Route.Item.class);
                String xml = xs.toXML(route);
            
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/xml");
                response.setCharacterEncoding("UTF-8");
                response.setContentLength(xml.getBytes().length);

                PrintWriter pw = response.getWriter();
                pw.print(xml);
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}