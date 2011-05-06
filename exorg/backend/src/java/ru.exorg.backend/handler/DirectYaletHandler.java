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


public class DirectYaletHandler extends YaletHandler {
    private Set<String> yalets;

    final public void setYaletList(final String[] yl) {
        this.yalets = new HashSet<String>();

        for (String y : yl) {
            this.yalets.add(y);
        }
    }

    final private static String xmlTemplate =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<page><yalet id=\"http://127.0.0.1:8081/%s\"/></page>";

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        String resName = target.substring(target.lastIndexOf("/") + 1);

        if (this.yalets.contains(resName)) {
            try {
                String s = String.format(xmlTemplate, resName);

                this.processor.process(request, response, s, false);
                baseRequest.setHandled(true);
            } catch (Exception e) {
                System.out.println("Exception was caught");
                e.printStackTrace();
            }
        }
    }
}