package eo.miner;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.regex.*;

import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.variables.Variable;
import org.webharvest.runtime.variables.NodeVariable;

import org.apache.log4j.*;

import eo.common.*;

// ================================================================================

public class POIMiner extends Miner {
    private POIProvider poiProvider;
    
    public POIMiner() { }

    final public void setDataProvider(final DataProvider p) {
        poiProvider = p.getPOIProvider();
    }

    protected void handle(final ScraperContext sc) throws Exception {
        String[] mined = sc.getVar("sights").toString().split("\\[Sight\\]\n");

        for (int i = 1; i < mined.length; ++i) {
            Vars v = parseMinedItem(mined[i]);
            
            if (v.containsKey("City") && v.containsKey("Name")) {
                POIProvider.Entry e = poiProvider.add(v.get("Name"));
                e.setCity(v.get("City"));
                
                if (v.containsKey("Image")) {
                    e.addRawImages(v.get("Image").split("\n"));
                }

                if (v.containsKey("Description")) {
                    e.addRawDescr(v.get("Description"), v.get("Source"));
                }
                if (v.containsKey("Address")) {
                    e.setAddress(v.get("Address").trim());
                }
                if (v.containsKey("Site")) {
                    e.setURL(v.get("Site"));
                }
            }
        }
    }
}

// ================================================================================

