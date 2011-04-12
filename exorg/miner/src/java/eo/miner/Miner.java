package eo.miner;

import org.apache.log4j.Logger;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;

import java.util.TreeMap;

// ================================================================================

public abstract class Miner {
    private String[] configFiles;
    private String proxy;

    private Logger log;

    protected static class Vars extends TreeMap<String, String> { }

    protected static String beautify(final String str) {
        return str
            .trim()
            .replaceAll("^[\r\n \t]*", "")
            .replaceAll("[\r\n \t]*$", "")
            .replaceAll("[ \t]+", " ")
            .replaceAll("[\r\n]+[\r\n ]*", "\n");
    }

    protected static Vars parseMinedItem(final String info) {
        Vars v = new Vars();

        String[] fields = info.split("[\\[\\]]");

        for (int i = 1; i + 1 < fields.length; i += 2) {
            if (fields[i + 1].length() > 1) {
                v.put(fields[i], beautify(fields[i + 1]));
            }
        }

        return v;
    }


    public Miner() {
        this.log = Logger.getLogger(Main.class);
        this.proxy = "";
        this.configFiles = new String[0];
    }

    final public void setConfig(final String config) {
        this.configFiles = config.split(";");
    }

    final public void setProxy(final String proxy) {
        this.proxy = proxy;
    }

    final public void run() {
        try {
            for (int j = 0; j < this.configFiles.length; ++j) {
                this.log.info("Working on " + this.configFiles[j]);

                ScraperConfiguration config = 
                    new ScraperConfiguration(configFiles[j]);
                Scraper scraper = new Scraper(config, ".");

                if (this.proxy.length() > 0) {
                    String[] proxyCfg = this.proxy.split(":");
                    String proxyHost = proxyCfg[0];
                    int proxyPort = Integer.parseInt(proxyCfg[1]);
                    scraper.getHttpClientManager().setHttpProxy(proxyHost, proxyPort);
                }
        
                scraper.setDebug(false);
                scraper.execute();

                handle(scraper.getContext());
            }
        } catch (Exception e) {
            System.out.println("Exception was caught. See debug.log for details");
            this.log.warn(e.getMessage());
            this.log.warn(eo.common.util.Log.getCallStack(e));
        }
    }

    abstract protected void handle(final ScraperContext sc) throws Exception;
}
