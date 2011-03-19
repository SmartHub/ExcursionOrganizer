package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

// ================================================================================

public interface DynamicHandler {
    public static class Response {
        public Object result;
        public Map<String, Class> aliases;

        public Response() {
            aliases = new TreeMap<String, Class>();
        }

        public Response(final String r, final Map<String, Class> a) {
            result = r;
            aliases = a;
        }
    }

    public Response handle(final HttpServletRequest request);
}