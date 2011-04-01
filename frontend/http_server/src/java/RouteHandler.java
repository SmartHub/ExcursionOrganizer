package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

// ================================================================================

public class RouteHandler implements DynamicHandler {

	private SessionManager sm_;

	public void setSM(SessionManager sm) {
		sm_ = sm;
	}
    private static class Route {
        private static class POI {
            long id;
            String name;
            String address;
            double lat;
            double lng;

            public POI(Searcher.POI poi) {
                id = poi.id;
                name = poi.name;
                address = poi.address;
                lat = poi.lat;
                lng = poi.lng;
            }
        }

        public int id;
        public String descr;
        public POI[] pois;        

        public Route(Searcher.Route r) {
            id = r.id;
            descr = r.descr;

            pois = new POI[r.pois.length];

            for (int i = 0; i < r.pois.length; ++i) {
                try {
                    Searcher.POI p = Searcher.queryById(r.pois[i]);
                    pois[i] = new POI(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    pois[i] = null;
                }
            }
        }

		public Route(SessionManager.UserRoute r) {
			id = r.sid;
            
            pois = new POI[r.ps.length];

            for (int i = 0; i < r.ps.length; ++i) {
                try {
                    pois[i] = new POI(Searcher.queryById(r.ps[i].poi_id));
                } catch (Exception e) {
                    e.printStackTrace();
                    pois[i] = null;
                }
            }
		}
    }

    public Response handle(final Request request) {
        Response r = new Response();
		try {
            
            if (request.getParameterValues("id") != null) {
                int route_id = Integer.parseInt(request.getParameterValues("id")[0]);
                Searcher.Route raw_route = Searcher.queryRoute(route_id);
                Route route = new Route(raw_route);
                
                r.result = route;
                r.aliases.put("route", Route.class);
                r.aliases.put("poi", Route.POI.class);            
			} else {
				if (request.getParameterValues("sid") != null) {
                	int route_id = Integer.parseInt(request.getParameterValues("sid")[0]);
					SessionManager.UserRoute ur = sm_.getRoute(route_id);
                	Route route = new Route(ur);
                
                	r.result = route;
                	r.aliases.put("route", Route.class);
                	r.aliases.put("poi", Route.POI.class);
				}
			}

            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}
