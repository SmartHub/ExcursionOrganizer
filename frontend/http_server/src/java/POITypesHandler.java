package eo.frontend.httpserver;

import java.lang.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.*;
import javax.servlet.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

import com.thoughtworks.xstream.XStream;

// ================================================================================

public class POITypesHandler implements DynamicHandler {
    private static class Type {
        public String name;
    }

    public Response handle(final Request request) {
        try {
            Response r = new Response();

            String[] type_names = Searcher.queryTypes();
            Type[] t = new Type[type_names.length];
            for (int i = 0; i < type_names.length; ++i) {
                t[i] = new Type();
                t[i].name = type_names[i];
            }

            r.result = t;
            r.aliases.put("poi-types", Type[].class);
            r.aliases.put("type", Type.class);
            r.aliases.put("value", String.class);

            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return new Response();
        }
    }
}
