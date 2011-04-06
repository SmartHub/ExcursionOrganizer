package eo.miner;

import eo.common.dbwrapper.POI;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;

import java.util.TreeMap;

// ================================================================================

public class Main implements InitializingBean {
    private POI poi_;
    private String[] config_files_;
    private String proxy_;

    private Logger log;


    public Main() {
        log = Logger.getLogger(Main.class);
        proxy_ = "";
    }


    final public void setPOI(final POI poi) {
        poi_ = poi;
    }


    final public void setConfig(final String config) {
        config_files_ = config.split(";");
    }

    final public void setProxy(final String proxy) {
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
            if (fields[i + 1].length() > 1) {
                v.put(fields[i], beautify(fields[i + 1]));
            }
        }

        return v;
    }


    private static String beautify(final String str) {
        //final String spaces = "[\r\n\ \t]*";

        return str
            .trim()
            .replaceAll("^[\r\n \t]*", "")
            .replaceAll("[\r\n \t]*$", "")
            .replaceAll("[ \t]+", " ")
            .replaceAll("[\r\n]+[\r\n ]*", "\n");
    }


    public void afterPropertiesSet() {
        try {
            for (int j = 0; j < config_files_.length; ++j) {
                log.info("Working on" + config_files_[j]);

                ScraperConfiguration config = 
                    new ScraperConfiguration(config_files_[j]);
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
                //System.out.println(scraper.getContext().getVar("sights").toString());

                for (int i = 1; i < mined.length; ++i) {
                    Vars v = parseMinedItem(mined[i]);

                    if (v.containsKey("City") && v.containsKey("Name")) {
                        POI.Entry e = poi_.add(v.get("Name"));
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
        } catch (Exception e) {
            System.out.println("Exception was caught. See debug.log for details");
            log.warn(e.getMessage());
            log.warn(eo.common.util.Logger.getCallStack(e));
        }
    }
}

// ================================================================================
