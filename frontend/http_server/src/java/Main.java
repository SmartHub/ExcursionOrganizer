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
    
    public void afterPropertiesSet() throws Exception {
        Server s = new Server(new InetSocketAddress(bind_host_, bind_port_));

        s.setHandler(h_);

        s.start();
        s.join();
    }    
}