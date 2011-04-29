package ru.exorg.core.model;

import org.json.simple.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.*;

// ================================================================================

public final class Description implements JSONAware {
    private String text;
    private String sourceUrl;
    
    public Description(final String t, final String s) {
        this.text = t;
        this.sourceUrl = s;
    }

    public String getText() {
        return text;
    }

    public String getSourceURL() {
        return sourceUrl;
    }

    final public String toJSONString() {
        StringWriter sw = new StringWriter();
        sw.append("{");
        sw.append("\"text\"");
        sw.append(":");
        sw.append("\"" + JSONObject.escape(this.text) + "\"");
        sw.append(",");
        sw.append("\"source\"");
        sw.append(":");
        sw.append("\"" + JSONObject.escape(this.sourceUrl) + "\"");
        sw.append("}");
        return sw.toString();
    }
}