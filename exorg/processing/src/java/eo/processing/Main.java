package eo.processing;

import eo.db.CafeProvider;
import eo.db.DataProvider;
import eo.db.POIProvider;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.InitializingBean;

import java.net.URLEncoder;
import java.util.*;

// ================================================================================

public class Main implements InitializingBean {
    private DataProvider dataProvider;
    private POIProvider poiProvider;
    private CafeProvider cafeProvider;

    private HttpConnection conn;

    private String proxyHost = "";
    private int proxyPort = 0;
    
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
        return proxyHost.length() != 0;
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
            } else {
                /* Who knows why the connection to the proxy server suddenly closes? */
                if (!conn.isOpen()) {
                    conn.open();
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


    private static class GeoInfo {
        public String address;
        public DataProvider.Loc loc;

        public GeoInfo(final String addr, double lat, double lng) {
            this.address = addr;
            this.loc = new DataProvider.Loc(lat, lng);
        }
    }

    List<GeoInfo> parseGeoInfo(final JSONObject gi) {
        if (((String)gi.get("status")).equals("OK")) {
            List<GeoInfo> r = new ArrayList<GeoInfo>();
            JSONArray results = (JSONArray)gi.get("results");
            
            for (int i = 0; i < results.size(); ++i) {
                JSONObject first_res = (JSONObject)results.get(i);
                String address = (String)first_res.get("formatted_address");
                JSONObject loc = (JSONObject)((JSONObject)first_res.get("geometry")).get("location");
                double lat = ((Double)loc.get("lat")).doubleValue();
                double lng = ((Double)loc.get("lng")).doubleValue();

                r.add(new GeoInfo(address, lat, lng));
            }
            return r;                    
        } else {
            return null;
        }
    }

    private void addRawGeo(POIProvider.Entry e) throws Exception {
        if (!e.hasRawGeo()) {
            JSONObject r;

            if (e.hasAddress()) {            
                r = queryGAPI(new String[]{"address", e.getAddress()});
            } else {
                r = queryGAPI(new String[]{"address", e.getName()});
            }

            List<GeoInfo> lgi = parseGeoInfo(r);
            if (lgi != null) {
                for (GeoInfo gi : lgi) {
                    if (dataProvider.isWithinCity(e.getCityId(), gi.loc)) {
                        e.addRawGeoInfo(gi.address, gi.loc.lat, gi.loc.lng);
                    }
                }
            }
            Thread.sleep(500);
        }
    }

    private void addGeoInfo(CafeProvider.Cafe cafe) throws Exception {
        DataProvider.Address[] as = cafe.getAddresses();
        if (as != null) {
            for (DataProvider.Address a : as) {
                if (!cafe.hasGeo(a.address)) {
                    /* Google won't find a cafe by its name */

                    JSONObject r = queryGAPI(new String[]{"address", a.address});
                    List<GeoInfo> lgi = parseGeoInfo(r);
                    if (lgi != null) {
                        for (GeoInfo gi : lgi) {
                            if (dataProvider.isWithinCity(cafe.getCityId(), gi.loc)) {
                                cafe.setGeoInfo(a.address, gi.address, gi.loc.lat, gi.loc.lng);
                            }
                        }
                    }
                    
                    Thread.sleep(500);
                }
            }
        }
    }

    private void guessType(POIProvider.Entry e) {
        if (!e.hasType()) {
            e.guessType();
        }
    }

    private void processPOI() throws Exception {
        Iterator<POIProvider.Entry> it = poiProvider.poiIterator();
        while (it.hasNext()) {
            POIProvider.Entry e = it.next();

            addRawGeo(e);
            guessType(e);
        }
    }

    private void processCafes() throws Exception {
        Iterator<CafeProvider.Cafe> it = cafeProvider.cafeIterator();
        while (it.hasNext()) {
            CafeProvider.Cafe cafe = it.next();
            
            addGeoInfo(cafe);
        }
    }

    public void afterPropertiesSet() {
        try {
            //processPOI();
            processCafes();            
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}