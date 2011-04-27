package ru.exorg.processing;

import java.lang.*;
import java.util.*;
import java.net.*;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.springframework.beans.factory.InitializingBean;

import ru.exorg.core.service.*;
import ru.exorg.core.model.*;

// ================================================================================

final public class Main implements InitializingBean {
    private DataProvider dataProvider;
    private POIProvider poiProvider;
    private CafeProvider cafeProvider;
    private List<String> poiNames;
    private int timeout = 10000;
    private int clusterLevel = 1;

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

    public void setClusterLevel(int cl) {
        this.clusterLevel = cl;
    }

    private boolean doesUseProxy() {
        return proxyHost != null;
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
            conn.close(); // whew an timeout occurs no data can be read from the connection later
            conn = null;

            throw e;
        }

        /*
        if (!doesUseProxy()) {
            conn = null;
        }
        */

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
            System.out.println("Quering for " + loc.getAddress());
        } else if (guess != null) {
            r = queryGAPI(new String[]{"address", guess});
            System.out.println("Quering for " + guess);
        } else {
            return false;
        }

        List<Location> locs = parseGeoInfo(r);
        if (locs == null && guess != null && loc.getAddress() != null) {
            r = queryGAPI(new String[]{"address", guess});
            System.out.println("Quering for " + guess);
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
        } else {
            System.out.println("Failed");
        }

        return false;
    }

    private void addGeoInfo(POI poi) throws Exception {
        if (!poi.getLocation().isValid()) {
            if (poi.hasAddress()) {
                poi.setAddress(poi.getAddress().replaceAll("^\\d+,\\s*", "").replaceAll("\\(.*?\\)", ""));
            }

            lookupLocation(poi.getLocation(), poi.getName());

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

    private static long max(long v1, long v2) {
        if (v1 > v2) {
            return v1;
        } else {
            return v2;
        }
    }

    private void clusterize1() {
        Iterator<POI> it = poiProvider.poiIterator();

        while (it.hasNext()) {
            POI poi = it.next();

            if (poi.hasAddress()) {
                List<POI> poiList = poiProvider.queryByAddress(poi.getAddress());

                if (poiList != null) {
                    if (!poiProvider.isInCluster(poiList.get(0)) && poiList.size() >= 2) {
                        long cid = poiProvider.getMaxClusterId() + 1;
                        for (POI p : poiList) {
                            poiProvider.setPOICluster(p, cid);
                        }
                    }
                }
            }
        }

        POIProvider.Clusters clusters = this.poiProvider.getClusters();
        for (List<Long> cluster : clusters.values()) {
            for (int i = 0; i < cluster.size(); i++) {
                boolean remove = true;
                POI curPOI = poiProvider.queryById(cluster.get(i));
                String cur = curPOI.getName();

                for (int j = i + 1; j < cluster.size(); j++) {
                    String other = poiProvider.queryById(cluster.get(j)).getName();

                    if ((float)Util.getLevenshteinDistance(cur, other)/max(cur.length(), other.length()) < 0.6) {
                        remove = false;
                        break;
                    }
                }

                if (remove) {
                    poiProvider.removeFromCluster(curPOI);
                }
            }
        }
    }

    private void clusterize2() {
        Iterator<POI> it = poiProvider.poiIterator();

        while (it.hasNext()) {
            float m = 1;
            POI nearest = null;
            POI cur = it.next();

            for (String s : this.poiNames) {
                POI other = this.poiProvider.queryByName(s);

                if (other.getId() != cur.getId()) {
                    String n = cur.getName();

                    float cm = (float)Util.getLevenshteinDistance(n, s)/max(s.length(), n.length());
                    if (cm < 0.1 && cm < m) {
                        m = cm;
                        nearest = other;
                        //cid = this.poiProvider.getPOICluster(other);
                    }

                    if (cm > 1) {
                        System.out.println("Nonsence, edit distance is " + String.valueOf(cm));
                    }
                }
            }

            if (nearest != null) {
                long cid = this.poiProvider.getPOICluster(nearest);
                if (cid != 0) {
                    this.poiProvider.setPOICluster(cur, cid);
                } else {
                    cid = this.poiProvider.getMaxClusterId() + 1;
                    this.poiProvider.setPOICluster(cur, cid);
                    this.poiProvider.setPOICluster(nearest, cid);

                    System.out.println("Adding POI #" + String.valueOf(nearest.getId()) + " into cluster #" + String.valueOf(cid));
                }

                System.out.println("Adding POI #" + String.valueOf(cur.getId()) + " into cluster #" + String.valueOf(cid) + "; min edit distance is " + String.valueOf(m));
            } else {
                System.out.println("Not clustering POI #" + String.valueOf(cur.getId()));
            }
        }
    }

    private void processPOI() throws Exception {
        this.poiNames = this.poiProvider.getPOINames();

        this.poiProvider.clearClusters();

        Iterator<POI> it = poiProvider.poiIterator();
        while (it.hasNext()) {
            POI poi = it.next();

            /*
            try {
                this.addGeoInfo(poi);
                this.guessType(poi);

                this.poiProvider.sync(poi);
            } catch (SocketTimeoutException e) {
                System.out.println("Failed to retrieve geographic information for " + poi.getName());
            }
            */
        }

        if (clusterLevel >= 1) {
            this.clusterize1();
            this.poiProvider.collapseClusters();

            if (clusterLevel >= 2) {
                this.clusterize2();
                this.poiProvider.collapseClusters();
            }
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