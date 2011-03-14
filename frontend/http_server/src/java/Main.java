package eo.frontend.httpserver;

import java.lang.*;
import java.net.*;

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

    public Main(final Handler[] handlers, final String iface) {
        String[] iface_cfg = iface.split(":");
        bind_host_ = iface_cfg[0];
        bind_port_ = Integer.parseInt(iface_cfg[1]);

        hc_ = new HandlerCollection();
        hc_.setHandlers(handlers);        
    }

    public void afterPropertiesSet() throws Exception {
        Server s = new Server(new InetSocketAddress(bind_host_, bind_port_));
        s.setHandler(hc_);

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