package ru.exorg.backend.handler;

import net.sf.xfresh.core.YaletProcessor;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

public class YaletHtmlPageHandler extends YaletHandler {
    private YaletProcessor yaletProcessor;
    private SessionManager sm;
    private Map<String, String> yalets;

    final public void setYaletProcessor(final YaletProcessor p) {
        this.yaletProcessor = p;
    }

    final public void setSessionManager(SessionManager sm) {
        this.sm = sm;
    }

    final public void setYaletList(final String[] yl) {
        Map<String, String> m = new TreeMap<String, String>();
        for (String h : yl) {
            m.put(h, h);
        }
        this.setYaletMap(m);
    }

    final public void setYaletMap(final Map<String, String> ym) {
        this.yalets = ym;
    }

    final private static String xmlTemplate =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"%s.xsl\"?>\n" +
            "<page><yalet id=\"%s\"/></page>";

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        String resName = target.substring(target.lastIndexOf("/") + 1, target.lastIndexOf("."));

        //System.out.println("Yalet Html page handler was called on " + target);

        if (yalets.containsKey(resName)) {
            if (target.endsWith(".html")) {
                try {
                    String s = String.format(xmlTemplate, resName, this.yalets.get(resName));
                    process(target, baseRequest, request, response, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}