package ru.exorg.backend.handler;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class LoggingHandler extends AbstractHandler {
    private static Logger log = Logger.getLogger(LoggingHandler.class);
    private HandlerCollection hc;

    /*
    public void setHandlers(final Handler[] handlers) {
        hc = new HandlerCollection();
        hc.setHandlers(handlers);

        for (Handler h : handlers) {
            System.out.println(h.getClass().toString());
        }
    }
    */

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        log.debug("Requesting page " + target);
        //hc.handle(target, baseRequest, request, response);
    }
}