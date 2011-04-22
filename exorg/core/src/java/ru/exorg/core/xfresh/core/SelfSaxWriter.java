package ru.exorg.core.xfresh.core;

import org.xml.sax.SAXException;

/**
 * User: darl (darl@yandex-team.ru)
 * Date: 3/21/11 8:29 PM
 */
public interface SelfSaxWriter {
    void writeTo(String externalName, SaxHandler saxHandler) throws SAXException;
}
