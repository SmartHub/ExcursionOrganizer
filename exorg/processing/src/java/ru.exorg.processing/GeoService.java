package ru.exorg.processing;

import java.util.*;
import java.net.URLEncoder;

import org.springframework.beans.factory.InitializingBean;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ru.exorg.core.model.Location;

final public class GeoService implements InitializingBean {
    final private static int    httpPort = 80;
    final private static String GAPI_PROTO = "http://";
    final private static String GAPI_SERVER = "maps.googleapis.com";
    final private static String GAPI_PATH = "/maps/api/geocode/json?";
    final private static String GAPI_Q_FOOTER = "&sensor=true";

    private int timeout = 10000;

    private HttpConnection conn;

    private String proxyHost;
    private int proxyPort;


    public void setProxy(final String proxy) {
        String[] proxyConf = proxy.split(":");
        this.proxyHost = proxyConf[0];
        this.proxyPort = Integer.parseInt(proxyConf[1]);
    }

    private boolean doesUseProxy() {
        return this.proxyHost != null;
    }

    private String queryHttp(final String q) throws Exception {
        String hostName = GAPI_SERVER;
        String path = GAPI_PATH + q + GAPI_Q_FOOTER;

        HttpState state = new HttpState();

        boolean needOpen = false;
        if (conn == null) {
            if (this.doesUseProxy()) {
                conn = new HttpConnection(proxyHost, proxyPort, hostName, httpPort);
            } else {
                conn = new HttpConnection(hostName, httpPort);
            }
            needOpen = true;
        } else {
            /* Who knows why the connection to the proxy server suddenly closes? */
            if (!conn.isOpen()) {
                needOpen = true;
            }
        }

        if (needOpen) {
            conn.open();
            conn.getParams().setSoTimeout(this.timeout);
            conn.getParams().setConnectionTimeout(this.timeout);
        }

        HttpMethod getMeth = new GetMethod();
        getMeth.setPath(path);
        getMeth.addRequestHeader("Host", "maps.googleapis.com");
        getMeth.addRequestHeader("Accept", "application/json");
        getMeth.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US)");
        //getMeth.addRequestHeader("Accept-Language", "ru");
        getMeth.addRequestHeader("Accept-Language", "ru;en");
        getMeth.addRequestHeader("Accept-Charset", "utf-8");

        if (!doesUseProxy()) {
            getMeth.addRequestHeader("Connection", "close");
        } else {
            getMeth.addRequestHeader("Proxy-Connection", "keep-alive");
        }

        try {
            getMeth.execute(state, conn);
        } catch (Exception e) {
            conn.close(); // when an timeout occurs no data can be read from the connection later
            conn = null;

            throw e;
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


    public List<Location> parseGeoInfo(final JSONObject gi) {
        if (gi == null || gi.get("status ") == null) {
            System.out.println("Invalid geographic info");

            return null;
        }

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

    public List<Location> lookupLocation(Location loc, final String guess) throws Exception {
        JSONObject r;

        if (loc.getAddress() != null) {
            r = queryGAPI(new String[]{"address", loc.getAddress()});
        } else if (guess != null) {
            r = queryGAPI(new String[]{"address", guess});
        } else {
            return null;
        }

        List<Location> locs = parseGeoInfo(r);
        if (locs == null && guess != null && loc.getAddress() != null) {
            r = queryGAPI(new String[]{"address", guess});
            locs = parseGeoInfo(r);
        }

        return locs;
    }

    public void afterPropertiesSet() { }
}