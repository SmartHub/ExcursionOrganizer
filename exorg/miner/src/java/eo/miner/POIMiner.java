package eo.miner;

import org.webharvest.runtime.ScraperContext;

import eo.service.DataProvider;
import eo.service.POIProvider;

import eo.model.*;

// ================================================================================

public class POIMiner extends Miner {
    private POIProvider poiProvider;
    
    public POIMiner() { }

    protected void handle(final ScraperContext sc) throws Exception {
        this.poiProvider = dataProvider.getPOIProvider();

        String[] mined = sc.getVar("sights").toString().split("\\[Sight\\]\n");
        for (String item : mined) {
            Vars v = parseMinedItem(item);
            
            if (v.containsKey("City") && v.containsKey("Name")) {
                POI poi = poiProvider.add(v.get("Name"));
                poi.setCityId(this.dataProvider.getCityId(v.get("City")));
                
                if (v.containsKey("Image")) {
                    for (String image : v.get("Image").split("\n")) {
                        poi.addImage(image);
                    }
                }

                if (v.containsKey("Description")) {
                    poi.addDescription(v.get("Description"), v.get("Source"));
                }
                if (v.containsKey("Address")) {
                    poi.setAddress(v.get("Address").trim());
                }
                if (v.containsKey("Site")) {
                    poi.setURL(v.get("Site"));
                }
                
                this.poiProvider.sync(poi);
            }
        }

        
    }
}

// ================================================================================

