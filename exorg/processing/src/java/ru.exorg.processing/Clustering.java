package ru.exorg.processing;

import ru.exorg.core.model.POI;
import ru.exorg.core.service.DataProvider;
import ru.exorg.core.service.POIProvider;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Clustering {
    private Map<Long, Long> clusters;
    private long maxClusterId;
    private POIProvider poiProvider;

    public interface Clusters extends Map<Long, List<Long>> { }
    private class _Clusters extends TreeMap<Long, List<Long>> implements Clusters { }

    public Clustering() {
        this.clusters = new TreeMap<Long, Long>();
    }

    final public void setDataProvider(DataProvider p) {
        this.poiProvider = p.getPOIProvider();
    }

    final public void clearClusters() {
        this.clusters.clear();
        this.maxClusterId = 1;
    }

    final public boolean isInCluster(final POI poi) {
        return clusters.containsKey(poi.getId());
    }

    final public long getPOICluster(final POI poi) {
        if (this.clusters.containsKey(poi.getId())) {
            return this.clusters.get(poi.getId());
        } else {
            return 0;
        }
    }

    final public long getMaxClusterId() {
        return this.maxClusterId;
    }

    final public void setPOICluster(final POI poi, long clusterId) {
        if (clusterId > 0) {
            this.clusters.put(poi.getId(), clusterId);

            if (clusterId > this.maxClusterId) {
                this.maxClusterId = clusterId;
            }
        } else {
            this.clusters.put(poi.getId(), this.maxClusterId + 1);
            this.maxClusterId++;
        }
    }

    final public void removeFromCluster(final POI poi) {
        this.clusters.remove(poi.getId());
    }

    final public Clusters getClusters() {
        Clusters icl = new _Clusters();

        for (Map.Entry<Long, Long> e : clusters.entrySet()) {
            if (!icl.containsKey(e.getValue())) {
                icl.put(e.getValue(), new ArrayList<Long>());
            }

            icl.get(e.getValue()).add(e.getKey());
        }

        return icl;
    }

    /*
    final public void collapseClusters() throws Exception {
        Clusters icl = this.getClusters();

        for (Map.Entry<Long, List<Long>> e : icl.entrySet()) {
            if (e.getValue().size() >= 2) {
                Iterator<Long> it = e.getValue().iterator();
                POI cur = this.queryById(it.next());

                System.out.println("Merging:");
                while (it.hasNext()) {
                    POI other = this.queryById(it.next());
                    cur.addDescriptions(other.getDescriptions());
                    cur.addImages(other.getImages());

                    this.removePOI(other);

                    System.out.println(other.getName() + " " + String.valueOf(other.getId()));
                }

                System.out.println(cur.getName() + " " + String.valueOf(cur.getId()) + "\n\n");
                this.sync(cur); // Atomicity?
            }
        }

        this.clearClusters();
    }*/

    final public void commitClusters() throws Exception {
        Clusters icl = this.getClusters();

        for (Map.Entry<Long, List<Long>> c : icl.entrySet()) {
            long clusterId = c.getKey();
            System.out.println("Merging");

            for (long poiId : c.getValue()) {
                POI poi = this.poiProvider.queryById(poiId);
                poi.setClusterId(clusterId);
                this.poiProvider.sync(poi);

                System.out.println(poi.getName());
            }
        }

        long cid = this.maxClusterId;
        for (POI poi : this.poiProvider.poiList()) {
            if (!this.isInCluster(poi)) {
                poi.setClusterId(cid);
                poi.setClusterHeadFlag(true);
                cid++;

                this.poiProvider.sync(poi);
            }
        }
    }
}