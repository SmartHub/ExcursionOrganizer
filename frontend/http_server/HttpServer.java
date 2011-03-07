import java.io.*;
import java.net.*;
import java.lang.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

// ================================================================================
 
public class HttpServer extends AbstractHandler
{
    private static final Class rman = HttpServer.class;


    private static URL getRes(final String rname) {
        return rman.getResource(rname);
    }

    private static InputStream getResStream(final String rname) {
        return rman.getResourceAsStream(rname);
    }

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
            /*fr.read(buf);
        
        
        
        */
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(new InetSocketAddress("localhost", 8080));
        server.setHandler(new HttpServer());
 
        server.start();
        server.join();
    }
}