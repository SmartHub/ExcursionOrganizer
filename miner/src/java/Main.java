package eo.miner;

import java.util.*;
import java.net.URL;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.runtime.variables.NodeVariable;

import org.springframework.beans.factory.InitializingBean;


import eo.common.POI;

import java.nio.charset.Charset;

// ================================================================================

public class Main implements InitializingBean {
    private POI poi_;
    private String config_;


    public void setPOI(final POI poi) {
        poi_ = poi;  
    }


    public void setConfig(final String config) {
        config_ = config;
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

            poi_.add("name", "descr");

            /*
            ScraperConfiguration config = 
                new ScraperConfiguration(config_);
            Scraper scraper = new Scraper(config, ".");
        
            scraper.setDebug(true);
            scraper.execute();

            String[] mined = scraper.getContext().getVar("sights").toString().split("\\[Name\\]\n");

            for (int i = 0; i < mined.length; i++) {
                String[] sight = mined[i].split("\\[Description\\]\n");
                if (sight.length >= 2) {
                    poi_.add(sight[0].trim(), sight[1].trim());
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