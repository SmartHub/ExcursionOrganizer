package eo.frontend.httpserver;

import com.thoughtworks.xstream.converters.extended.ToStringConverter;
import org.eclipse.jetty.server.Request;

// ================================================================================

public class IndexHandler implements DynamicHandler {
    private static class Route {
        
        private static class POI_ID {
            private int v_;

            public POI_ID() {
            }

            public POI_ID(final java.lang.String s) {
            }

            public POI_ID(int v) {
                v_ = v;
            }

            public String toString() {
                return String.valueOf(v_);
            }
        }

        public int id;
        public POI_ID[] pois;
        public String descr;
        public String img;

        public Route(Searcher.Route r) {
            id = r.id;
            descr = r.descr;

            pois = new POI_ID[r.pois.length];
            for (int i = 0; i < r.pois.length; ++i) {
                pois[i] = new POI_ID(r.pois[i]);
            }
            
            try {
                img = Searcher.queryById(r.pois[0]).img_url;
            } catch (Exception e) {
                e.printStackTrace();
                img = "";
            }
        }
    }

    public Response handle(final Request request) {
        try {
            Response r = new Response();

            Searcher.Route[] raw_rec_routes = Searcher.queryRoutes();
            Route[] rec_routes = new Route[raw_rec_routes.length];
            for (int i = 0; i < raw_rec_routes.length; ++i) {
                rec_routes[i] = new Route(raw_rec_routes[i]);
            }

            r.result = rec_routes;
            r.aliases.put("routes", Route[].class);
            r.aliases.put("route", Route.class);
            r.aliases.put("poi", Route.POI_ID.class);

            r.convs.add(new ToStringConverter(Route.POI_ID.class));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}