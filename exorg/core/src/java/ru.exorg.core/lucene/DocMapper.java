package ru.exorg.core.lucene;

import org.apache.lucene.document.Document;

public interface DocMapper<T> {
    public T mapDoc(final Document document);
}