package ru.exorg.frontend.handler;

import java.lang.*;
import java.util.*;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RedirectHandler extends AbstractHandler {
    private Map<String, String> redirectionMap;

    public void setRedirectionMap(final Map<String, String> rm) {
        this.redirectionMap = rm;
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        String path = baseRequest.getUri().getPath();
        if (this.redirectionMap.containsKey(path)) {
            System.out.println("Redirecting to " + this.redirectionMap.get(path));
            response.sendRedirect(this.redirectionMap.get(path));
            baseRequest.setHandled(true);
        }
    }
}