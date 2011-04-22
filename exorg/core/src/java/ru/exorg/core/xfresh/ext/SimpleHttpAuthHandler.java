package ru.exorg.core.xfresh.ext;

import ru.exorg.core.xfresh.core.InternalRequest;
import ru.exorg.core.xfresh.core.InternalResponse;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: Olga Bolshakova (obolshakova@yandex-team.ru)
 * Date: 02.01.11 22:07
 */
public class SimpleHttpAuthHandler implements AuthHandler {

    private String authUrl;

    private String userIdXpath;

    @Required
    public void setUserIdXpath(final String userIdXpath) {
        this.userIdXpath = userIdXpath;
    }

    @Required
    public void setAuthUrl(final String authUrl) {
        this.authUrl = authUrl;
    }

    public void processAuth(final InternalRequest req, final InternalResponse res, final ContentHandler handler) {
        try {
            final InputStream content = getAuthInfoStream(collectCookiesHeader(req));
            final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setXIncludeAware(true);
            final SAXParser saxParser = parserFactory.newSAXParser();
            final XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
            xmlReader.setContentHandler(ContentWriter.wrap(handler));
            xmlReader.parse(new InputSource(content));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getAuthInfoStream(final Map<String, String> headers) throws IOException {
        return new HttpLoader().loadAsStream(authUrl, 300, Collections.<String, List<String>>emptyMap(), headers);
    }

    private Map<String, String> collectCookiesHeader(final InternalRequest req) {
        final Map<String, String> cookies = req.getCookies();
        if (cookies.isEmpty()) {
            return Collections.emptyMap();
        }
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> en : cookies.entrySet()) {
            sb.append(en.getKey()).append("=").append(en.getValue()).append(";");
        }
        return Collections.singletonMap("Cookie", sb.substring(0, sb.length() - 1));
    }

    public Long getUserId(final InternalRequest req) {
        try {
            final String result = XPathFactory.newInstance().newXPath().evaluate(userIdXpath, new InputSource(getAuthInfoStream(collectCookiesHeader(req))));
            if (result == null || result.isEmpty()) {
                return null;
            }
            return Long.valueOf(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
