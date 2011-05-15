package ru.exorg.backend.handler;

import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class YaletHtmlPageHandler extends YaletHandler {
    private static Logger log = Logger.getLogger("Performance");

    private Map<String, String> yalets;

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
                    "<?xml-stylesheet type=\"text/xsl\" href=\"http://127.0.0.1:8080/%s.xsl\"?>\n" +
                    "<page><yalet id=\"%s\"/></page>";

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        String resName = target.substring(target.lastIndexOf("/") + 1, target.lastIndexOf("."));

        if (yalets.containsKey(resName)) {
            if (target.endsWith(".html")) {
                try {
                    HttpSession hs = request.getSession(true);

                    long start = System.currentTimeMillis();
                    log.debug(String.format("Started handling request for %s from %s (%s)", target, request.getRemoteAddr(), hs.getId()));

                    String s = String.format(xmlTemplate, resName, this.yalets.get(resName));
                    this.processor.process(request, response, s, true);
                    baseRequest.setHandled(true);

                    long stop = System.currentTimeMillis();
                    log.debug(String.format("Handling request for %s from %s (%s) finished for %d ms", target, request.getRemoteAddr(), hs.getId(), stop - start));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}