package eo.processing;

import java.util.*;
import java.net.*;
import java.io.*;

import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.InitializingBean;

import org.json.simple.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import eo.common.POI;

// ================================================================================

public class Main implements InitializingBean{
    private POI poi_;

    private String GAPI_PROTO = "http://";
    private String GAPI_SERVER = "maps.googleapis.com";
    private String GAPI_PATH = "/maps/api/geocode/json?";
    private String GAPI_Q_FOOTER = "&sensor=true";


    private JSONObject queryGAPI(final Map<String, String> query) throws Exception {
        /*
          URL url = new URL(GAPI_PROTO + GAPI_SERVER + GAPI_PATH + query + GAPI_Q_FOOTER);
          
          URLConnection conn = url.openConnection();
          conn.addRequestProperty("Connection", "close");
          conn.addRequestProperty("Cache-Control", "max-age=0");
          conn.addRequestProperty("Accept", "application/json");
          conn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US)");
          conn.addRequestProperty("Accept-Language", "ru"); 
          conn.addRequestProperty("Accept-Charset", "utf-8");

          conn.setDoInput(true);
          byte buf[] = new byte[conn.getContentLength()];
          ((InputStream)conn.getContent()).read(buf);
          
          System.out.println(new String(buf));
        */

        StringBuilder qs = new StringBuilder();
        for (Map.Entry<String, String> e : query.entrySet()) {
            qs.append(e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
        }
        String host_name = GAPI_SERVER;
        String path = GAPI_PATH + qs.toString() + GAPI_Q_FOOTER;

        HttpState state = new HttpState();
        HttpConnection conn = new HttpConnection(host_name, 80);
        HttpMethod get_meth = new GetMethod();
        conn.open();
        get_meth.setPath(path);
        get_meth.addRequestHeader("Host", "maps.googleapis.com");
        get_meth.addRequestHeader("Connection", "close");
        get_meth.addRequestHeader("Cache-Control", "max-age=0");
        get_meth.addRequestHeader("Accept", "application/json");
        get_meth.addRequestHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US)");
        get_meth.addRequestHeader("Accept-Language", "ru"); 
        get_meth.addRequestHeader("Accept-Charset", "utf-8");
        get_meth.execute(state, conn);
        
        /*
          StringBuilder out = new StringBuilder();
          GZIPInputStream s = new GZIPInputStream(new ByteArrayInputStream(get_meth.getResponseBody()));
          byte[] buf = new byte[1024];
          while (s.read(buf) > 0) {
          out.append(buf);
          }
        */
        return (JSONObject)JSONValue.parse(new String(get_meth.getResponseBody()));
    }

    public Main(POI poi) {
        poi_ = poi;
    }

    public void afterPropertiesSet() {
        try {
            Map<String, String> p = new TreeMap<String, String>();
            p.put("address", "");
        

            Iterator it = poi_.poiIterator();
            while (it.hasNext()) {
                POI.Entry e = (POI.Entry)it.next();

                p.put("address", e.name());
                JSONObject r = queryGAPI(p);

                if (((String)r.get("status")).equals("OK")) {
                    JSONArray results = (JSONArray)r.get("results");
                    JSONObject first_res = (JSONObject)results.get(0);
                    String address = (String)first_res.get("formatted_address");
                    
                    e.setAddress(address.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}