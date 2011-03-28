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
    private SessionManager sm_;

    public void setSM(SessionManager sm) {
        sm_ = sm;
    }

    private void writeXMLResponse(final String xml, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength(xml.getBytes().length);
                    
        PrintWriter pw = response.getWriter();
        pw.print(xml);
        pw.flush();
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException {
        try {
            if (target.substring(target.lastIndexOf("/") + 1).equals("route")) {
                if (baseRequest.getParameterValues("sid") != null) {
                    if (baseRequest.getParameterValues("poi_list") != null) {
                        // We have to update the user route

                        String[] pois = baseRequest.getParameterValues("poi_list")[0].split(",");
                        SessionManager.UserRoute r = new SessionManager.UserRoute(pois.length);
                        r.sid = Integer.parseInt(baseRequest.getParameterValues("sid")[0]);

                        //System.out.println(r.ps.length);

                        for (int i = 0; i < pois.length; ++i) {
                            //System.out.println(pois[i]);
                            r.ps[i] = new SessionManager.UserRoute.Point(Integer.parseInt(pois[i]), 0);

                            //System.out.println(String.valueOf(r.ps[i].poi_id));
                        }

                        sm_.setRoute(r);

                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        // We should return the specified user route
                        int sid = Integer.parseInt(baseRequest.getParameterValues("sid")[0]);
                        
                        SessionManager.UserRoute route = sm_.getRoute(sid);
                        XStream xs = new XStream();
                        xs.alias("route", SessionManager.UserRoute.class);
                        xs.alias("points", SessionManager.UserRoute.Point[].class);
                        xs.alias("point", SessionManager.UserRoute.Point.class);
                        String xml = xs.toXML(route);

                        writeXMLResponse(xml, response);
                    }
                } else {
                    int route_id = Integer.parseInt(baseRequest.getParameterValues("id")[0]);
                    Searcher.Route route = Searcher.queryRoute(route_id);

                    XStream xs = new XStream();
                    xs.alias("route", Searcher.Route.class);
                    xs.alias("poi", int.class);
                    //xs.alias("item", Searcher.Route.Item.class);
                    String xml = xs.toXML(route);
            
                    writeXMLResponse(xml, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}