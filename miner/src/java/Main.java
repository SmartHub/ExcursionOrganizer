package ExcursionOrganizer.Miner;

import java.util.*;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.runtime.variables.NodeVariable;


import ExcursionOrganizer.DB.*;

import java.nio.charset.Charset;

// ================================================================================

public class Main {
    private static final Class rman = Main.class;


    private static URL getRes(final String rname) {
        return rman.getResource(rname);
    }


    private static class MinedItem {
        public String name_;
        public String desc_;
        
        public MinedItem(final String name, final String desc) {
            name_ = name;
            desc_ = desc;
        }
    }


    public static void main(String[] args) {
        try {
            DOMConfigurator.configure(getRes("/log4j.xml"));

            ScraperConfiguration config = 
                new ScraperConfiguration("../../config/imhotour-ru.cfg.xml");
            Scraper scraper = new Scraper(config, ".");
        
            scraper.setDebug(true);
            
            scraper.execute();

            String[] mined = scraper.getContext().getVar("sights").toString().split("\\[Name\\]\n");

            for (int i = 0; i < mined.length; i++) {
                String[] sight = mined[i].split("\\[Description\\]\n");
                if (sight.length >= 2) {
                    DBWrapper.insertPlaceOfInterest(new PlaceOfInterest(sight[0].trim(), sight[1].trim()));
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