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

            /*
            DBWrapper.insertPlaceOfInterest(new PlaceOfInterest("Один", "Два"));

            PlaceOfInterest p1 = new PlaceOfInterest("Один", "Два");
            System.out.println(p1.getName());

            List<PlaceOfInterest> lst = DBWrapper.getAllPlaceOfInterest();
            for (PlaceOfInterest poi : lst) {
                System.out.println(poi.getName());
            }
            */

            DBWrapper.insertPlaceOfInterest(new PlaceOfInterest("test", "test"));

            ScraperConfiguration config = 
                new ScraperConfiguration("../../config/imhotour-ru.cfg.xml");
            Scraper scraper = new Scraper(config, ".");
        
            scraper.setDebug(true);
            
            scraper.execute();

            //String[] mined = scraper.getContext().getVar("sights").toString().split("\\[Name\\]\n");
            System.out.println(scraper.getContext().getVar("sights").toString());
            //LinkedList<MinedItem> sights = new LinkedList<MinedItem>();
            /*
            for (int i = 0; i < mined.length; i++) {
                String[] sight = mined[i].split("\\[Description\\]\n");
                if (sight.length >= 2) {
                    if (sight[1].length() < 1000) {
                        DBWrapper.insertPlaceOfInterest(new PlaceOfInterest(sight[0].trim(), sight[1].trim()));
                    } else {
                        System.out.println(sight[0] + "! ");
                    }
                }
            }
            */

            

            /*
            for (MinedItem it : sights) {
                System.out.println(it.name_ + ": " + it.desc_);
            }
            */
            
            //System.out.println(scraper.getContext().getVar("num_of_pages").toString());
        } 
        catch(java.lang.Exception e) {
            System.out.println("Exception was caught");
            System.out.println(e.getMessage());
        }
    }
}

// ================================================================================