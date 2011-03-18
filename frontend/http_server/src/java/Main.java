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

// ================================================================================
 
public class Main implements InitializingBean {
    private HandlerCollection hc_;
    private String bind_host_;
    private int bind_port_;

    
    private class TestServer extends AbstractHandler {
        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) 
            throws IOException, ServletException {

            //System.out.println("TestServer was called");
        
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/xml");
            response.setCharacterEncoding("UTF-8");

            String s = "<test></test>";
            response.setContentLength(s.length());
            
            //response.setHeader("Content-Type", "application/xml");
            PrintWriter w = response.getWriter();
            w.print(s);
            w.flush();
        }
    }

    private static ContextHandler wrap(final String path, final AbstractHandler handler) {
        ContextHandler ch = new ContextHandler();

        ch.setContextPath(path);
        ch.setHandler(handler);
        return ch;
    }
    

    public Main(final Handler[] handlers, final String iface) {
        String[] iface_cfg = iface.split(":");
        bind_host_ = iface_cfg[0];
        bind_port_ = Integer.parseInt(iface_cfg[1]);

        //hc_ = new HandlerCollection();
        //hc_.setHandlers(handlers);        
    }

    public void afterPropertiesSet() throws Exception {
        Server s = new Server(new InetSocketAddress(bind_host_, bind_port_));
        //s.setHandler(hc_);

        HandlerCollection hc = new ContextHandlerCollection();


        AbstractHandler test_handler = new TestServer();
        hc.addHandler(wrap("/test", test_handler));


        ResourceHandler static_handler = new ResourceHandler();
        static_handler.setResourceBase("frontend/web");
        hc.addHandler(wrap("/content", static_handler));

        s.setHandler(hc);

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