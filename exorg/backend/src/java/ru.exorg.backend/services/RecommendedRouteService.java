package ru.exorg.backend.services;

import java.util.List;
import java.util.ArrayList;

import org.springframework.jdbc.core.JdbcTemplate;
import org.apache.lucene.document.Document;

import ru.exorg.backend.model.Route;
import ru.exorg.backend.model.RoutePoint;

import ru.exorg.core.lucene.*;


/**
 * Created by IntelliJ IDEA.
 * User: lana
 * Date: 23.04.11
 * Time: 3:37
 * To change this template use File | Settings | File Templates.
 */

public class RecommendedRouteService {
    private Search searcher;
    private PoiService poiService;
    private RRMapper rrMapper;

    private class RRMapper implements DocMapper<Route> {
        public Route mapDoc(Document doc) {
            List<RoutePoint> poiList = new ArrayList<RoutePoint>();
            String[] pois_raw = doc.get("poiList").split(" ");

            for (int i = 0; i < pois_raw.length; i++) {
                long poiId = Long.parseLong(pois_raw[i]);
                poiList.add(new RoutePoint(poiService.getPoiById(poiId), i + 1));
            }

            Route route = new Route(doc.get("id"), doc.get("name"), doc.get("description"));
            route.setPoints(poiList);

            return route;
        }
    }

    public RecommendedRouteService() {
        rrMapper = new RRMapper();
    }

    public void setSearcher(Search s) {
        this.searcher = s;
    }

    public void setPoiService(PoiService ps) {
        this.poiService = ps;
    }

    public Route getRecommendedRoute(final long id) throws Exception
    {
        return this.searcher.search(
                String.format("DocType:\"%s\" AND id:%d", Indexer.DocTypeReadyRoute, id),
                this.rrMapper).get(0);
    }

    public List<Route> getRecommendedRouteList()  throws Exception
    {
        return this.searcher.search(
                String.format("DocType:\"%s\"", Indexer.DocTypeReadyRoute),
                this.rrMapper);
    }
}