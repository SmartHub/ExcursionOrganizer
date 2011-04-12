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

class CafeMiner extends Miner {
    CafeProvider cafeProvider;

    public CafeMiner() { }

    final public void setDataProvider(final DataProvider p) {
        cafeProvider = p.getCafeProvider();
    }    

    protected void handle(final ScraperContext sc) throws Exception {
        String[] mined = sc.getVar("cafes").toString().split("\\[Cafe\\]\n");

        for (int i = 1; i < mined.length; ++i) {            
            Vars v = parseMinedItem(mined[i]);

            if (v.containsKey("City")) {
                CafeProvider.Cafe cafe = cafeProvider.add(v.get("Name"));

                cafe.setCity(v.get("City"));
                if (v.containsKey("Description")) {
                    cafe.setDescription(v.get("Description"), v.get("Source"));
                }
                if (v.containsKey("Address")) {
                    String[] addresses = v.get("Address").split("\\n");
                    for (String address : addresses) {
                        int cp = address.indexOf(':');
                        if (cp == -1) {
                            cafe.setAddress(address);
                        } else {
                            String a = address.substring(cp);
                            if (a.length() > 1) {
                                cafe.setAddress(a);
                            }
                        }                        
                    }
                }
                if (v.containsKey("Site")) {
                    cafe.setURL(v.get("Site"));
                }
                if (v.containsKey("Cuisine")) {
                    cafe.setCuisine(v.get("Cuisine").replaceAll("\\n", ""));
                }
            }
        }
    }
}
