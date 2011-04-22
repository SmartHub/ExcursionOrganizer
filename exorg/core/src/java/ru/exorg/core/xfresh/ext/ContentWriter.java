package ru.exorg.core.xfresh.ext;

import org.apache.log4j.Logger;
import org.apache.xalan.xsltc.trax.DOM2SAX;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Date: Dec 1, 2010
 * Time: 11:06:37 PM
 *
 * @author Nikolay Malevanny nmalevanny@yandex-team.ru
 */
public class ContentWriter {
    private static final Logger log = Logger.getLogger(ContentWriter.class);

    private final ContentHandler contentHandler;

    public ContentWriter(final ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public void write(@NotNull final String content) {
        final char[] chars = content.toCharArray();
        try {
            contentHandler.characters(chars, 0, chars.length);
        } catch (SAXException e) {
            log.error("ERROR", e); //ignored
        }
    }

    public void writeLoaded(final LoadedXml xml) {
        writeNode(xml.getNode());
    }

    public void writeNode(final Node node) {
        if (node == null) {
            log.debug("Can't write empty node");
            return;
        }
        try {
//            final ToXMLSAXHandler xmlSaxHandler = new ToXMLSAXHandler(contentHandler, "UTF-8");
//            xmlSaxHandler.serialize(node);
            final DOM2SAX dom2SAX = new DOM2SAX(node);
            dom2SAX.setContentHandler(wrap(contentHandler));
            dom2SAX.parse();
        } catch (Exception e) {
            log.error("Can't write node", e); //ignored
        }
    }

    public static ContentHandler wrap(final ContentHandler req) {
        return (ContentHandler) Proxy.newProxyInstance(req.getClass().getClassLoader(), new Class[]{ContentHandler.class}, new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                if ("startDocument".equals(method.getName()) || "endDocument".equals(method.getName())) {
                    return null;
                }
                return method.invoke(req, args);
            }
        });
    }
}
