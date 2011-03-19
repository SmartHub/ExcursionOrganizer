package eo.frontend.httpserver;

import java.lang.*;
import java.net.*;
import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

import org.springframework.beans.factory.InitializingBean;

import com.thoughtworks.xstream.XStream;

// ================================================================================
 
public class Main implements InitializingBean {
    //private HandlerCollection hc_;
    private AbstractHandler h_;
    private String bind_host_;
    private int bind_port_;

    /*
    private static class TestServer extends AbstractHandler {
        private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\n";

        private static class POI {
            private String name;
            private String description;
            private String location;

            public POI(String n, String d, String l) {
                name = n;
                description = d;
                location = l;
            }
        }

        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) 
            throws IOException, ServletException {

            //System.out.println("TestServer was called");

            XStream xs = new XStream();
            POI[] ps = new POI[3];
            ps[0] = new POI("poi1", "poi1 description", "10 10");
            ps[1] = new POI("poi2", "poi2 description", "40 11");
            ps[2] = new POI("poi3", "poi3 description", "60 12");
            
            xs.alias("poi", POI.class);
            xs.alias("poi-list", POI[].class);
            String s = XML_HEADER + xs.toXML(ps);

            //System.out.println(s);
        
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(s.length());
            
            PrintWriter w = response.getWriter();
            w.print(s);
            w.flush();
        }
    }
    */

    private static ContextHandler wrap(final String path, final AbstractHandler handler) {
        ContextHandler ch = new ContextHandler();

        ch.setContextPath(path);
        ch.setHandler(handler);
        return ch;
    }


    public void setInterface(final String iface) {
        String[] iface_cfg = iface.split(":");

        bind_host_ = iface_cfg[0];
        bind_port_ = Integer.parseInt(iface_cfg[1]);
    }

    public void setHandler(final AbstractHandler h) {
        h_ = h;
    }
    
    /*
    public Main(final Handler[] handlers, final String iface) {

        //hc_ = new HandlerCollection();
        //hc_.setHandlers(handlers);        
    }
    */

    public void afterPropertiesSet() throws Exception {
        Server s = new Server(new InetSocketAddress(bind_host_, bind_port_));
      
        /*
        HandlerCollection hc = new ContextHandlerCollection();

        AbstractHandler test_handler = new TestServer();
        hc.addHandler(wrap("/test", test_handler));


        ResourceHandler static_handler = new ResourceHandler();
        static_handler.setResourceBase("frontend/web");
        hc.addHandler(wrap("/content", static_handler));
        */

        s.setHandler(h_);

        s.start();
        s.join();
    }

    /*
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

        //System.out.println(getRes("/cityplaces.html").toString());
            
            InputStream s = getResStream("/cityplaces.html");
            byte[] buf = new byte[s.available()];
            s.read(buf);
            response.getWriter().println(new String(buf));
            
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(new InetSocketAddress("localhost", 8080));
        server.setHandler(new HttpServer());
 
        server.start();
        server.join();
    }
    */
    
}