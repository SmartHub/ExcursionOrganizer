package ru.exorg.backend.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.HashSet;

import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.xfresh.core.YaletSupport;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;


public class DirectYaletHandler extends AbstractHandler {
    private YaletSupport yaletSupport;
    private Set<String> yalets;
    private static final OutputFormat of = new OutputFormat("XML", "utf-8", false);

    final public void setYaletSupport(final YaletSupport ys) {
        this.yaletSupport = ys;
    }

    final public void setYaletList(final String[] yl) {
        this.yalets = new HashSet<String>();

        for (String y : yl) {
            this.yalets.add(y);
        }
    }

    private XMLReader createReader() throws ParserConfigurationException, SAXException {
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setXIncludeAware(true);
        final SAXParser saxParser = parserFactory.newSAXParser();
        final XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
        return xmlReader;
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        String resName = target.substring(target.lastIndexOf("/") + 1);

        if (this.yalets.contains(resName)) {
            try {
                String t = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<page><yalet id=\"%s\"/></page>";
                String s = String.format(t, resName);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                XMLFilter f = this.yaletSupport.createFilter(this.yaletSupport.createRequest(request,target), this.yaletSupport.createResponse(response));
                f.setParent(this.createReader());
                f.setContentHandler(new XMLSerializer(stream, of));
                f.parse(new InputSource(new StringReader(s)));
                response.getOutputStream().write(stream.toByteArray());
                response.getOutputStream().flush();
                baseRequest.setHandled(true);
            } catch (Exception e) {
                System.out.println("Exception was caught");
                e.printStackTrace();
            }
        }
    }
}