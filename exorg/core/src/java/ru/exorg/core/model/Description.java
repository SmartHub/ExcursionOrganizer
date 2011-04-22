package ru.exorg.core.model;
import java.lang.*;

// ================================================================================

public final class Description {
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
}