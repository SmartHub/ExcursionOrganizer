package ru.exorg.core.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;

// ================================================================================

public class Starter implements InitializingBean, ApplicationContextAware {
    private AbstractHandler handlers;
    private String bindHost;
    private int bindPort;
    private ApplicationContext ac;

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

    final public void setApplicationContext(ApplicationContext ac) {
        this.ac = ac;
    }

    final public void afterPropertiesSet() {
        Server s = new Server(new InetSocketAddress("0.0.0.0", bindPort));
        s.setHandler(handlers);

        try {
            s.start();
            s.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

