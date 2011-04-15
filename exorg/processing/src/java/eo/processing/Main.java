package eo.processing;

<<<<<<< HEAD
import java.net.URLEncoder;
import java.util.*;

=======
import eo.db.CafeProvider;
import eo.db.DataProvider;
import eo.db.POIProvider;
>>>>>>> 1ac6ecb21f34ddf23c335499c772a0d750c1ca1e
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.beans.factory.InitializingBean;

import eo.db.*;
import eo.model.*;

// ================================================================================

final public class Main implements InitializingBean {
    private DataProvider dataProvider;
    private POIProvider poiProvider;
    private CafeProvider cafeProvider;

    private HttpConnection conn;

    private String proxyHost;
    private int proxyPort;
    
    private int    httpPort = 80;
    private String GAPI_PROTO = "http://";
    private String GAPI_SERVER = "maps.googleapis.com";
    private String GAPI_PATH = "/maps/api/geocode/json?";
    private String GAPI_Q_FOOTER = "&sensor=true";


    public void setDataProvider(DataProvider p) {
        this.dataProvider = p;
        this.poiProvider = p.getPOIProvider();
        this.cafeProvider = p.getCafeProvider();
    }

    public void setProxy(final String proxy) {
        String[] proxyConf = proxy.split(":");
        proxyHost = proxyConf[0];
        proxyPort = Integer.parseInt(proxyConf[1]);
    }

    private boolean doesUseProxy() {
        return proxyHost != null;
    }

    private String queryHttp(final String q) throws Exception {
        String hostName = GAPI_SERVER;
        String path = GAPI_PATH + q + GAPI_Q_FOOTER;

        HttpState state = new HttpState();

        if (!doesUseProxy()) {
            conn = new HttpConnection(hostName, httpPort);
            conn.open();
        } else {
            if (conn == null) {
                conn = new HttpConnection(proxyHost, proxyPort, hostName, httpPort);
                conn.open();
                conn.getParams().setSoTimeout(5000);
            } else {
                /* Who knows why the connection to the proxy server suddenly closes? */
                if (!conn.isOpen()) {
                    conn.open();
                    conn.getParams().setSoTimeout(5000);
                }
            }
        }
        HttpMethod getMeth = new GetMethod();
        getMeth.setPath(path);
        getMeth.addRequestHeader("Host", "maps.googleapis.com");
        getMeth.addRequestHeader("Accept", "application/json");
        getMeth.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US)");
        getMeth.addRequestHeader("Accept-Language", "ru"); 
        getMeth.addRequestHeader("Accept-Charset", "utf-8");

        if (!doesUseProxy()) {
            getMeth.addRequestHeader("Connection", "close");
        } else {
            getMeth.addRequestHeader("Proxy-Connection", "keep-alive");
        }
        getMeth.execute(state, conn);

        if (!doesUseProxy()) {
            conn = null;
        }

        return new String(getMeth.getResponseBody());
    }


    private JSONObject queryGAPI(final Map<String, String> query) throws Exception {
        StringBuilder qs = new StringBuilder();
        for (Map.Entry<String, String> e : query.entrySet()) {
            qs.append(e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
        }
        
        return (JSONObject)JSONValue.parse(queryHttp(qs.toString()));
    }

    private JSONObject queryGAPI(final String[] query) throws Exception {
        Map<String, String> p = new TreeMap<String, String>();

        for (int i = 0; i < query.length; i += 2) {
            p.put(query[i], query[i+1]);
        }

        return queryGAPI(p);
    }


    private List<Location> parseGeoInfo(final JSONObject gi) {
        if (((String)gi.get("status")).equals("OK")) {
            List<Location> r = new ArrayList<Location>();
            JSONArray results = (JSONArray)gi.get("results");
            
            for (int i = 0; i < results.size(); ++i) {
                JSONObject first_res = (JSONObject)results.get(i);
                String address = (String)first_res.get("formatted_address");
                JSONObject loc = (JSONObject)((JSONObject)first_res.get("geometry")).get("location");
                double lat = ((Double)loc.get("lat")).doubleValue();
                double lng = ((Double)loc.get("lng")).doubleValue();

                r.add(new Location(0, address, lat, lng));
            }
            return r;                    
        } else {
            return null;
        }
    }

    private boolean lookupLocation(Location loc, final String guess) throws Exception {
        JSONObject r;

        if (loc.getAddress() != null) {
            r = queryGAPI(new String[]{"address", loc.getAddress()});
        } else if (guess != null) {
            r = queryGAPI(new String[]{"address", guess});
        } else {
            return false;
        }

        List<Location> locs = parseGeoInfo(r);
        if (locs == null && guess != null && loc.getAddress() != null) {
            r = queryGAPI(new String[]{"address", guess});
            locs = parseGeoInfo(r);
        }

        if (locs != null) {
            for (Location l : locs) {
                if (dataProvider.isWithinCity(loc.getCityId(), l)) {
                    loc.setAddress(l.getAddress());
                    loc.setLat(l.getLat());
                    loc.setLng(l.getLng());
                    return true;
                }
            }
        }

        return false;
    }

    private void addGeoInfo(POI poi) throws Exception {
        if (!poi.getLocation().isValid()) {
            if (poi.hasAddress()) {
                poi.setAddress(poi.getAddress().replaceAll("^\\d+,\\s*", ""));
            }

            if (lookupLocation(poi.getLocation(), poi.getName())) {
                //System.out.println("Updating address " + poi.getLocation().getAddress());
            }

            Thread.sleep(500);
        }
    }

    private void addGeoInfo(Cafe cafe) throws Exception {
        for (Location loc : cafe.getLocations()) {
            if (!loc.isValid() && loc.getAddress() != null) {
                /* Google won't find a cafe by its name */
                lookupLocation(loc, null);
                Thread.sleep(500);
            }
        }
    }


    private void guessType(POI poi) throws Exception {
        if (!poi.hasType()) {
            dataProvider.guessPOIType(poi);
        }
    }

    private void processPOI() throws Exception {
        Iterator<POI> it = poiProvider.poiIterator();
        while (it.hasNext()) {
            POI poi = it.next();

            addGeoInfo(poi);
            guessType(poi);

            poiProvider.sync(poi);
        }
    }

    private void processCafes() throws Exception {
        Iterator<Cafe> it = cafeProvider.cafeIterator();
        while (it.hasNext()) {
            Cafe cafe = it.next();            
            addGeoInfo(cafe);

            cafeProvider.sync(cafe);
        }
    }

    public void afterPropertiesSet() {
        try {
            processPOI();
            //processCafes();            
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}