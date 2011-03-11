package eo.miner;

import java.util.*;
import java.lang.*;
import java.util.regex.*;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.runtime.variables.NodeVariable;

import org.springframework.beans.factory.InitializingBean;


import eo.common.POI;

// ================================================================================

public class Main implements InitializingBean {
    private POI poi_;
    private String config_;
    private String proxy_;


    public Main() {
        proxy_ = "";
    }


    public void setPOI(final POI poi) {
        poi_ = poi;  
    }


    public void setConfig(final String config) {
        config_ = config;
    }

    public void setProxy(final String proxy) {
        proxy_ = proxy;
    }


    private static class MinedItem {
        public String name_;
        public String desc_;
        
        public MinedItem(final String name, final String desc) {
            name_ = name;
            desc_ = desc;
        }
    }


    private static class Vars extends TreeMap<String, String> { }
    
    private static Vars parseMinedItem(final String info) {
        Vars v = new Vars();

        String[] fields = info.split("[\\[\\]]");
        for (int i = 1; i + 1 < fields.length; i += 2) {
            //System.out.println(fields[i] + ":" + fields[i+1]);
            if (fields[i+1].length() > 0) {
                v.put(fields[i], fields[i+1]);
            }
        }

        /*Pattern p = Pattern.compile("(\\[.+?\\])", Pattern.DOTALL);      

        Matcher m = p.matcher(data);
        if (m.matches()) {
            do {
                System.out.println(m.group(1));
                //System.out.println(m.group(1) + ":" + m.group(2));
                //v.put(m.group(1), m.group(2));
            } while(m.find());
        }
        */

        return v;
    }


    public void afterPropertiesSet() {
        try {
            ScraperConfiguration config = 
                new ScraperConfiguration(config_);
            Scraper scraper = new Scraper(config, ".");

            if (proxy_.length() > 0) {
                String[] proxy_cfg = proxy_.split(":");
                String proxy_host = proxy_cfg[0];
                int proxy_port = Integer.parseInt(proxy_cfg[1]);
                scraper.getHttpClientManager().setHttpProxy(proxy_host, proxy_port);
            }
        
            scraper.setDebug(true);
            scraper.execute();

            String[] mined = scraper.getContext().getVar("sights").toString().split("\\[Sight\\]\n");

            for (int i = 1; i < mined.length; ++i) {
                Vars v = parseMinedItem(mined[i]);

                POI.Entry e = poi_.add(v.get("Name"));

                if (v.containsKey("Description")) {
                    e.addRawDescr(v.get("Description"), v.get("Source"));
                }
                if (v.containsKey("Address")) {
                    e.setAddress(v.get("Address"));
                }
                if (v.containsKey("Site")) {
                    e.setURL(v.get("Site"));
                }
            }

            /*
            Pattern p = Pattern.compile("\\s*(.+)\\s*\\[Description\\]\\s*(.+)\\s*\\[Source\\]\\s*(.+)\\s*", Pattern.DOTALL);
            for (int i = 1; i < mined.length; i++) {
                Matcher m = p.matcher(mined[i]);
                if (m.matches()) {
                    //System.out.println(m.group(1) + ": " +  m.group(2) + ": "  + m.group(3));
                    String name = m.group(1);
                    String descr = m.group(2).replaceAll("[ \t\f\r]+", " ").replaceAll("\n+", "\n");
                    String descr_src = m.group(3);
                    poi_.add(name).addRawDescr(descr, descr_src);
                } else {
                    //System.out.println("No match!");
                }
            }
            */
        } 
        catch(java.lang.Exception e) {
            System.out.println("Exception was caught");
            System.out.println(e.getMessage());
        }
    }
}

// ================================================================================