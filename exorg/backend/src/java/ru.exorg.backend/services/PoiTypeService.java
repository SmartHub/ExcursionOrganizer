package ru.exorg.backend.services;

import java.util.List;

import org.apache.lucene.document.Document;

import ru.exorg.core.model.PoiType;
import ru.exorg.core.lucene.*;

/**
 * Created by IntelliJ IDEA.
 * User: kate
 * Date: 04-May-2011
 * Time: 23:45:41
 * To change this template use File | Settings | File Templates.
 */
public class PoiTypeService {
    private static POITypeMapper poiTypeMapper = new POITypeMapper();
    private Search searcher;

    private static class POITypeMapper implements DocMapper<PoiType> {
        public PoiType mapDoc(Document doc) {
            try {
                 return new PoiType(doc.get("id"), doc.get("name"));
            } catch (Exception e) {
                 return new PoiType(0, "");
            }
        }
    }

    public PoiTypeService() { }

    public void setSearcher(Search s) {
        this.searcher = s;
    }

    public List<PoiType> getPoiTypes()
    {
        return this.searcher.search("DocType: " + Indexer.DocTypePOIType, this.poiTypeMapper);
    }
}
