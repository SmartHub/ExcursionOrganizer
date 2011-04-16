package eo.frontend.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.util.*;
import java.net.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xfresh.core.*;

// ================================================================================

public class Starter implements InitializingBean {
    private AbstractHandler handlers;
    private String bindHost;
    private int bindPort;

    public static class YaletHtmlPageHandler extends AbstractHandler {
        private YaletProcessor yaletProcessor;
        private Map<String, String> yalets;

        public YaletHtmlPageHandler() {
            this.yalets = new HashMap<String,String>();
            this.yalets.put("index", "index");
        }

        @Required
        public void setYaletProcessor(final YaletProcessor p) {
            this.yaletProcessor = p;
        }

        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException, ServletException {
            String resName = target.substring(target.lastIndexOf("/") + 1, target.lastIndexOf("."));

            System.out.println("Yalet Html page handler was called on " + target);

            if (yalets.containsKey(resName)) {
                if (target.endsWith(".html")) {
                    try {
                        this.yaletProcessor.process(request, response, "http://127.0.0.1:8080/" + resName + ".xml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    baseRequest.setHandled(true);
                } else if (target.endsWith(".xml")) {
                    String t = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<?xml-stylesheet type=\"text/xsl\"?>\n" +
                            "<page><yalet id=\"%s\"/></page>";
                    String s = String.format(t, this.yalets.get(resName));

                    response.setCharacterEncoding("UTF-8");
                    response.setContentLength(s.getBytes().length);
                    response.setStatus(HttpServletResponse.SC_OK);

                    PrintWriter pw = response.getWriter();
                    pw.print(s + "\n\n");
                    pw.flush();

                    baseRequest.setHandled(true);
                }
            }
        }
    }

    /*
    private static ContextHandler wrap(final String path, final AbstractHandler handler) {
        ContextHandler ch = new ContextHandler();

        ch.setContextPath(path);
        ch.setHandler(handler);
        return ch;
    }
    */

    public void setInterface(final String iface) {
        String[] ifaceCfg = iface.split(":");

        bindHost = ifaceCfg[0];
        bindPort = Integer.parseInt(ifaceCfg[1]);
    }

    public void setHandlers(final Handler[] handlers) {
        HandlerCollection hc = new HandlerCollection();
        hc.setHandlers(handlers);

        this.handlers = hc;
    }

    final public void afterPropertiesSet() {
        Server s = new Server(new InetSocketAddress(bindHost, bindPort));

        s.setHandler(handlers);

        try {
            s.start();
            s.join();
        } catch (Exception e) {
        }
    }
}

