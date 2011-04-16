package eo.model;

import java.lang.*;
import java.util.*;

// ================================================================================

final public class POI {
    private long id;
    private String name;
    private long type;

    private String url;
    
    private List<Description> descriptions;
    private List<String> images;

    private Location location;

    public POI(long id, final String name) {
        this.id = id;
        this.name = name;
        this.type = 1;
        
        this.descriptions = new ArrayList<Description>();
        this.images = new ArrayList<String>();

        this.location = new Location();
    }

    final public long getId() {
        return this.id;
    }

    final public String getName() {
        return this.name;
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

    final public void addDescription(final String text, final String source) {
        this.descriptions.add(new Description(text, source));
    }

    final public Location getLocation() {
        return this.location;
    }

    final public void setLocation(double lat, double lng) {
        this.location.setLat(lat);
        this.location.setLng(lng);
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

    final public void setType(int type) {
        this.type = type;
    }

    public boolean hasType() {
        return this.type != 1;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void addImage(final String image) {
        this.images.add(image);
    }
}