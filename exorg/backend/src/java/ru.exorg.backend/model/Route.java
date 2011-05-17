package ru.exorg.backend.model;

import ru.exorg.core.model.POI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 15.04.11
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class Route {

    private long id;

    private String description;

    private int countPoints;

    private double duration;

    private List<RoutePoint> points = null;

    private void init(long id, String description, int countPoints, double duration, List<RoutePoint> points) {
        this.id = id;
        this.description = description;
        this.countPoints = countPoints;
        this.duration = duration;
        this.points = points;
    }

    public Route(long id, String description, int countPoints, double duration, List<RoutePoint> points) {
        this.init(id, description, countPoints, duration, points);
    }

    public Route(long id, String description, int countPoints, double duration) {
        this.init(id, description, countPoints, duration, new ArrayList<RoutePoint>());
    }

    public Route(final String id, final String description) {
        this.init(Long.parseLong(id), description, 0, 0, new ArrayList<RoutePoint>());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountPoints() {
        return countPoints;
    }

    public void setCountPoints(int countPoints) {
        this.countPoints = countPoints;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<RoutePoint> getPoints() {
        return points;
    }

    public void setPoints(List<RoutePoint> points) {
        if (this.points != null)
            this.points.clear();
        else
            this.points = new ArrayList<RoutePoint>();

        for (RoutePoint rp : points) {
            if (rp != null)
                this.points.add(rp);
        }
        countPoints = this.points.size();

        //this.points = points;
    }

    public void addPoint(RoutePoint newPoint)
    {
        points.add(newPoint);
    }

    public boolean deletePoint(POI poi) {
        for (RoutePoint routePoint: points) {
            if (routePoint.getPoi().equals(poi)) {
                points.remove(routePoint);
                return true;
            }
        }
        return false;
    }

    public String getImage () {
        try {
            for (RoutePoint p : points) {
                if (p.getPoi().getImage() != null) {
                    return p.getPoi().getImage();
                }
            }
        } catch (Exception e) {

        }

        return "";
    }
}


