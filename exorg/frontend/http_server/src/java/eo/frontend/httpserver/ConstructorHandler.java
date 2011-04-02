package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

// ================================================================================

public class ConstructorHandler implements DynamicHandler {

	private SessionManager sm_;

	public void setSM(SessionManager sm) {
		sm_ = sm;
	}


    private static class Route {
        private static class POI {
            long id;
            String name;
            double lat;
            double lng;

            public POI(SessionManager.UserRoute.Point poi) {
                id = poi.poi_id;
				try {

					Searcher.POI p = Searcher.queryById(poi.poi_id);
					name = p.name;
                	lat = p.lat;
                	lng = p.lng;

				} catch (Exception e) {
                    e.printStackTrace();
                }
	        }
        }

        public int sid;
        public Searcher.POI[] pois;        

        public Route(SessionManager.UserRoute r) {
            sid = r.sid;
            
            pois = new Searcher.POI[r.ps.length];

            for (int i = 0; i < r.ps.length; ++i) {
                try {
                    pois[i] = Searcher.queryById(r.ps[i].poi_id);
                } catch (Exception e) {
                    e.printStackTrace();
                    pois[i] = null;
                }
            }
        }
	

    }

    public Response handle(final Request request) {
        try {
            Response r = new Response();
			
			if ((request.getParameterValues("action") != null) ) {
				if ((request.getParameterValues("sid") != null) &&
						 (request.getParameterValues("sid")[0] != "") && 
							(request.getParameterValues("poi_id") != null)) {
					
					//we have to delete poi from existing user`s route
                	int poi_id = Integer.parseInt(request.getParameterValues("poi_id")[0]);
					int sid = Integer.parseInt(request.getParameterValues("sid")[0]);
				
					SessionManager.UserRoute ur = sm_.getRoute(sid);
					SessionManager.UserRoute newRoute = new SessionManager.UserRoute(ur.ps.length - 1);
					newRoute.sid = ur.sid;
					int j = 0;
					for (int i = 0; i < ur.ps.length; ++i) {
						if (ur.ps[i].poi_id != poi_id) {
							newRoute.ps[j] = ur.ps[i];
							++j;
						}
						
					}
					sm_.setRoute(newRoute);
                	Route route = new Route(newRoute);
                
                	r.result = route;
									
				}
			}
			else {
			
            	if ( (request.getParameterValues("sid") != null) && 
						(!request.getParameterValues("sid")[0].equals("")) && 
							(request.getParameterValues("poi_id") != null)  ) {
				
					//we have to add poi to existing user`s route
                	int poi_id = Integer.parseInt(request.getParameterValues("poi_id")[0]);
					int sid = Integer.parseInt(request.getParameterValues("sid")[0]);
				
					SessionManager.UserRoute ur = sm_.getRoute(sid);
					SessionManager.UserRoute newRoute = new SessionManager.UserRoute(ur.ps.length + 1);
					newRoute.sid = ur.sid;
					for (int i = 0; i < ur.ps.length; ++i) {
						newRoute.ps[i] = ur.ps[i];
					}
					newRoute.ps[ur.ps.length] = new SessionManager.UserRoute.Point(poi_id, 1);
					sm_.setRoute(newRoute);
                	Route route = new Route(newRoute);
                
                	r.result = route;
			     
				} else {
					if(((request.getParameterValues("sid") == null) || (request.getParameterValues("sid")[0] == "")) && 
							(request.getParameterValues("poi_id") != null)) {
					
						int sid = sm_.createSession();
						int poi_id = Integer.parseInt(request.getParameterValues("poi_id")[0]);
	
						sm_.addPOI(sid, poi_id, 1);
						SessionManager.UserRoute ur = sm_.getRoute(sid);
                		Route route = new Route(ur);
                
                		r.result = route;
						
					} 
				}
			}	
			
			r.aliases.put("route", Route.class);
            r.aliases.put("poi", Searcher.POI.class); 
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}
