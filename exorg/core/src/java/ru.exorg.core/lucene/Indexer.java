package ru.exorg.core.lucene;

import java.util.List;
import java.io.File;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.beans.factory.InitializingBean;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.apache.lucene.store.SimpleFSDirectory;

import ru.exorg.core.model.POI;
import ru.exorg.core.model.PoiType;
import ru.exorg.core.service.DataProvider;
import ru.exorg.core.service.POIProvider;

import org.springframework.jdbc.core.RowMapper;

public class Indexer implements InitializingBean  {
    private POIProvider poiProvider;
    private DataProvider dataProvider;
    private String indexDirectory;

    final public static String DocTypePOI = "1";
    final public static String DocTypePOIType = "2";
    final public static String DocTypeReadyRoute = "3";

    public Indexer() { }

    public void setDataProvider(DataProvider p) {
        this.poiProvider = p.getPOIProvider();
        this.dataProvider = p;
    }

    public void setDirectory(final String directory) {
        this.indexDirectory = directory;
    }

    private Field createField(final String name, final Object value) {
        if (value != null) {
            return new Field(name, value.toString(), Field.Store.YES, Field.Index.ANALYZED);
        } else {
            return new Field(name, "", Field.Store.YES, Field.Index.ANALYZED);
        }
    }

    private Document createDoc(final POI poi) {
        Document doc = new Document();

        doc.add(createField("DocType", DocTypePOI));
        doc.add(createField("id", poi.getId()));
        doc.add(createField("name", poi.getName()));
        doc.add(createField("type", dataProvider.getPoiType(poi.getType()).getName()));
        doc.add(createField("address", poi.getAddress()));
        doc.add(createField("lat", poi.getLocation().getLat()));
        doc.add(createField("lng", poi.getLocation().getLng()));
        doc.add(createField("squareId", poi.getSquareId()));
        doc.add(createField("clusterId", poi.getClusterId()));
        doc.add(createField("clusterHeadFlag", poi.isClusterHead()));

        if (poi.hasDescription()) {
            doc.add(createField("description", poi.getDescriptions().get(0).getText()));
            doc.add(createField("descriptionURL", poi.getDescriptions().get(0).getSourceURL()));
        } else {
            doc.add(createField("description", "N/A"));
            doc.add(createField("descriptionURL", "N/A"));
        }

        if (poi.hasImage()) {
            doc.add(createField("imageURL", poi.getImage()));
        } else {
            doc.add(createField("imageURL", ""));
        }

        return doc;
    }

    private Document createDoc(final PoiType poiType) {
        Document doc = new Document();

        doc.add(createField("DocType", DocTypePOIType));
        doc.add(createField("id", poiType.getId()));
        doc.add(createField("name", poiType.getName()));

        return doc;
    }

    private void indexPOI(IndexWriter iw) throws Exception {
        List<POI> poiList = this.poiProvider.poiList();
        for (POI poi : poiList) {
            if (poi.hasAddress() && poi.getLocation().isValid())
                iw.addDocument(createDoc(poi));
        }
    }

    private void indexPOITypes(IndexWriter iw) throws Exception {
        List<PoiType> poiTypes = this.dataProvider.getPoiTypes();
        for (PoiType poiType : poiTypes) {
            iw.addDocument(createDoc(poiType));
        }
    }

    private void indexReadyRoutes(IndexWriter iw) throws Exception {
        SqlRowSet rs = this.dataProvider.getJdbcTemplate().queryForRowSet("SELECT * FROM route_recommended");
        rs.first();
        while(!rs.isAfterLast()) {
            List<Long> pois = this.dataProvider.getJdbcTemplate().queryForList(
                    "SELECT poi_id FROM route_poi WHERE route_id = ? ORDER BY order_num",
                    new Object[]{rs.getLong("id")},
                    Long.class);

            String poiList = "";

            for (Long poi : pois) {
                if (this.poiProvider.queryById(poi).getLocation().isValid()) {
                    poiList += String.valueOf(poi) + " ";
                }
            }

            Document doc = new Document();

            doc.add(createField("DocType", DocTypeReadyRoute));
            doc.add(createField("id", rs.getLong("id")));
            doc.add(createField("name", rs.getString("name")));
            doc.add(createField("description", rs.getString("descr")));
            doc.add(createField("poiList", poiList));

            iw.addDocument(doc);

            rs.next();
        }

    }

    final public void afterPropertiesSet() {
        try {
            SimpleFSDirectory fsd = new SimpleFSDirectory(new File(this.indexDirectory));
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_30, new StandardAnalyzer(Version.LUCENE_30));
            IndexWriter iw = new IndexWriter(fsd, iwc);

            this.indexPOI(iw);
            this.indexPOITypes(iw);
            this.indexReadyRoutes(iw);

            iw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}