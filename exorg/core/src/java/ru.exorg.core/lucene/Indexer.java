package ru.exorg.core.lucene;

import org.springframework.beans.factory.InitializingBean;
import ru.exorg.core.model.POI;
import ru.exorg.core.model.PoiType;
import ru.exorg.core.service.DataProvider;
import ru.exorg.core.service.POIProvider;

import java.util.List;

import java.io.IOException;
import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.apache.lucene.store.SimpleFSDirectory;

public class Indexer implements InitializingBean  {
    private POIProvider poiProvider;
    private DataProvider dataProvider;
    private String indexDirectory;

    final public static String DocTypePOI = "1";
    final public static String DocTypePOIType = "2";

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

    final public void afterPropertiesSet() {
        try {
            SimpleFSDirectory fsd = new SimpleFSDirectory(new File(this.indexDirectory));
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_30, new StandardAnalyzer(Version.LUCENE_30));
            IndexWriter iw = new IndexWriter(fsd, iwc);

            List<POI> poiList = this.poiProvider.poiList();
            for (POI poi : poiList) {
                iw.addDocument(createDoc(poi));
            }

            List<PoiType> poiTypes = this.dataProvider.getPoiTypes();
            for (PoiType poiType : poiTypes) {
                iw.addDocument(createDoc(poiType));
            }

            iw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}