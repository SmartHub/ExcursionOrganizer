package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.eclipse.jetty.server.Request;

import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.*;

// ================================================================================

public interface DynamicHandler {
    public static class Response {
        public Object result;
        public Map<String, Class> aliases;
        public List<SingleValueConverter> convs;

        public Response() {
            aliases = new TreeMap<String, Class>();
            convs = new ArrayList<SingleValueConverter>();
        }

        public Response(final String r, final Map<String, Class> a) {
            result = r;
            aliases = a;
        }
    }

    public Response handle(final Request request);
}