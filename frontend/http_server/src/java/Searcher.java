package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;

import org.sphx.api.*;

// ================================================================================

public class Searcher {
    public static class POI {
        public String name;
        public String address;
        public String description;
        public String type;
        public String img_url;
        public double lat, lng;

        private final static int NAME = 0;
        private final static int TYPE = 1;
        private final static int ADDRESS = 2;
        private final static int DESCRIPTION = 3;
        private final static int IMG_URL = 4;
        private final static int LAT = 5;
        private final static int LNG = 6;


        public POI() {
        }
        
        @SuppressWarnings("unchecked")
        public POI(final SphinxMatch match) {
            ArrayList<String> inf = match.attrValues;

            name = inf.get(NAME);
            type = inf.get(TYPE);
            address = inf.get(ADDRESS);
            description = inf.get(DESCRIPTION);
            img_url = inf.get(IMG_URL);
            lat = Double.parseDouble(inf.get(LAT));
            lng = Double.parseDouble(inf.get(LNG));
        }
    }


    public static POI[] query(final String keyword) throws Exception{
        SphinxClient sc = new SphinxClient("localhost", 9312);

        SphinxResult qr = sc.Query(keyword);
        POI[] r = new POI[qr.matches.length];
        for (int i = 0; i < r.length; ++i) {
            r[i] = new POI(qr.matches[i]);
        }

        return r;
    }
}