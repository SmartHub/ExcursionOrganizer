package ru.exorg.core.model;

import java.lang.*;
import java.util.*;

// ================================================================================

final public class POI {
    private long id;
    private String name;
    private long type;
    private PoiType typeObj;

    private String url;

    private long clusterId;
    private boolean clusterHeadFlag;

    private List<Description> descriptions;
    private List<String> images;

    private int squareId;
    private Location location;

    private void init(long id, final String name) {
        this.id = id;
        this.name = name;
        this.type = 1;

        this.descriptions = new ArrayList<Description>();
        this.images = new ArrayList<String>();

        this.location = new Location();

        this.clusterId = 0;
        this.clusterHeadFlag = false;
        this.squareId = 0;
        this.url = "";
    }

    public POI(final String id, final String name) {
        init(Long.valueOf(id), name);
    }

    public POI(long id, final String name) {
        init(id, name);
    }

    public POI() {
       this.type = 1;
       this.descriptions = new ArrayList<Description>();
       this.images = new ArrayList<String>();
       this.clusterId = 0;
       this.clusterHeadFlag = false;
       this.squareId = 0;
       this.location = new Location();
       this.id = 0;
       this.name = "";
       this.url = "";
    }

    public PoiType getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(PoiType typeObj) {
        this.typeObj = typeObj;
    }

    final public long getId() {
        return this.id;
    }

    final public void setId(long id) {
        this.id = id;
    }

    final public String getName() {
        return this.name;
    }

    final public long getClusterId() {
        return this.clusterId;
    }

    final public void setClusterId(long cid) {
        this.clusterId = cid;

        if (this.location.isValid()) {
            this.setClusterHeadFlag(true);
        }
    }

    final public void setClusterId(String cid) {
        this.setClusterId(Long.valueOf(cid));
    }

    final public boolean isClusterHead() {
        return this.clusterHeadFlag;
    }

    final public void setClusterHeadFlag(boolean f) {
        this.clusterHeadFlag = f;
    }

    final public void setClusterHeadFlag(final String f) {
        this.setClusterHeadFlag(Boolean.valueOf(f));
    }

    final public long getCityId() {
        return this.location.getCityId();
    }

    final public void setCityId(long id) {
        this.location.setCityId(id);
    }    

    final public List<Description> getDescriptions() {
        return descriptions;
    }

    final public boolean hasDescription() {
        return this.descriptions.size() > 0;
    }

    final public void addDescription(final String text, final String source) {
        this.descriptions.add(new Description(text, source));
    }

    public void addDescription(Description description)
    {
        this.descriptions.add(description);
    }

    final public void addDescriptions(final List<Description> ds) {
        for (Description d : ds) {
            this.addDescription(d.getText(), d.getSourceURL());
        }
    }

    final public Location getLocation() {
        return this.location;
    }

    final public void setLocation(double lat, double lng) {
        this.location.setLat(lat);
        this.location.setLng(lng);
    }

    final public void setLocation(final String lat, final String lng) {
        this.setLocation(Double.valueOf(lat), Double.valueOf(lng));
    }

    final public void setLocation(final Location loc) {
        this.setLocation(loc.getLat(), loc.getLng());
    }

    final public void setAddress(final String address) {
        this.location.setAddress(address);
    }

    final public String getAddress() {
        return this.location.getAddress();
    }
    
    final boolean hasLocation() {
        return this.location.isValid();
    }

    final public boolean hasAddress() {
        return this.location.hasAddress();
    }

    final public String getURL() {
        return this.url;
    }

    final public void setURL(final String url) {
        this.url = url;
    }

    final public long getType() {
        return this.type;
    }

    final public void setType(long type) {
        this.type = type;
    }

    final public boolean hasType() {
        return this.type != 1;
    }

    final public List<String> getImages() {
        return this.images;
    }

    final public String getImage() {
        for (String img : images) {
            if (img.length() > 3) {
                return img;
            }
        }
        return "";
    }

    final public boolean hasImage() {
        return this.images.size() > 0;
    }

    final public void addImage(final String image) {
        this.images.add(image);
    }

    final public void addImages(final List<String> imgs) {
        for (String img : imgs) {
            this.addImage(img);
        }
    }

    final public int getSquareId() {
        return this.squareId;
    }

    final public void setSquareId(int sqn) {
        this.squareId = sqn;
    }

    final public void setSquareId(final String sqn) {
        this.setSquareId(Integer.valueOf(sqn));
    }
}