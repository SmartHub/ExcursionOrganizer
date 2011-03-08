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


    public void afterPropertiesSet() {
        try {
            ScraperConfiguration config = 
                new ScraperConfiguration(config_);
            Scraper scraper = new Scraper(config, ".");

            if (proxy_.length() > 0) {
                String[] proxy_cfg = proxy_.split(":");
                String proxy_host = proxy_cfg[0];
                int proxy_port = parseInt(proxy_cfg[1]);
                scraper.getHttpClientManager().setProxy(proxy_host, proxy_port);
            }
        
            scraper.setDebug(true);
            scraper.execute();

            String[] mined = scraper.getContext().getVar("sights").toString().split("\\[Name\\]\n");

            Pattern p = Pattern.compile("\\s*(.+)\\s*\\[Description\\]\\s*(.+)\\s*\\[Source\\]\\s*(.+)\\s*", Pattern.DOTALL);
            for (int i = 1; i < mined.length; i++) {
                Matcher m = p.matcher(mined[i]);
                if (m.matches()) {
                    //System.out.println(m.group(1) + ": " +  m.group(2) + ": "  + m.group(3));
                    poi_.add(m.group(1), m.group(2));
                } else {
                    //System.out.println("No match!");
                }
            }
        } 
        catch(java.lang.Exception e) {
            System.out.println("Exception was caught");
            System.out.println(e.getMessage());
        }
    }
}

// ================================================================================