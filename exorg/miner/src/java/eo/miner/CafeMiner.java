package eo.miner;

import org.webharvest.runtime.ScraperContext;

import eo.model.*;

import eo.db.CafeProvider;
import eo.db.DataProvider;

// ================================================================================

class CafeMiner extends Miner {
    CafeProvider cafeProvider;

    public CafeMiner() { }

    protected void handle(final ScraperContext sc) throws Exception {
        this.cafeProvider = dataProvider.getCafeProvider();

        String[] mined = sc.getVar("cafes").toString().split("\\[Cafe\\]\n");

        for (int i = 1; i < mined.length; ++i) {            
            Vars v = parseMinedItem(mined[i]);

            if (v.containsKey("City") && v.containsKey("Name")) {
                long cityId = dataProvider.getCityId(v.get("City"));
                Cafe cafe = cafeProvider.add(v.get("Name"));

                cafe.setCityId(Integer.parseInt(v.get("City")));

                if (v.containsKey("Description")) {
                    cafe.setDescription(v.get("Description"), v.get("Source"));
                }
                if (v.containsKey("Address")) {
                    String[] addresses = v.get("Address").split("\\n");
                    for (String address : addresses) {
                        int cp = address.indexOf(':');
                        if (cp == -1) {
                            cafe.addLocation(address);
                        } else {
                            String a = address.substring(cp);
                            if (a.length() > 1) {
                                cafe.addLocation(a);
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

                this.cafeProvider.sync(cafe);
            }
        }
    }
}
