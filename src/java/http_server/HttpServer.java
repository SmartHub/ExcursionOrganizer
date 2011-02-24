import java.net.*;
import java.io.*;

// ================================================================================

public class HttpServer {
    private static class FCGI {
        private static class FCGIHeader {
            private final static int p_version = 0;
            private final static int p_type = 1;
            private final static int p_requestIdB1 = 2;
            private final static int p_requestIdB0 = 3;
            private final static int p_contentLengthB1 = 4;
            private final static int p_contentLengthB0 = 5;
            private final static int p_paddingLength = 6;

            public int version;
            public int type;
            public int requestId;
            public int contentLength;
            public int paddingLength;
            public int reserved;

            public void construct(byte[] data) {
                version = data[p_version];
                type = data[p_type];
                requestId = data[p_requestIdB0] + (data[p_requestIdB1] << 8);
                contentLength = data[p_contentLengthB0] + (data[p_contentLengthB1] << 8);
                paddingLength = data[p_paddingLength];
            }

            public byte[] deconstruct() {
                byte[] buf = new byte[8];

                buf[p_version] = (byte)(version & 0xFF);
                buf[p_type] = (byte)(type & 0xFF);
                buf[p_requestIdB0] = (byte)(requestId & 0xFF);
                buf[p_requestIdB1] = (byte)((requestId >> 8) & 0xFF);
                buf[p_contentLengthB0] = (byte)(contentLength & 0xFF);
                buf[p_contentLengthB1] = (byte)((contentLength >> 8) & 0xFF);
                buf[p_paddingLength] = (byte)(paddingLength & 0xFF);

                return buf;
            }
        }

        private static void readRequest(Socket sock, FCGIHeader h) throws java.io.IOException {
            byte[] raw_h = new byte[8];

            sock.getInputStream().read(raw_h);
            h.construct(raw_h);
        }

        private static void writeResponse(Socket sock, FCGIHeader req_fcgi_header, byte[] response) throws java.io.IOException {
            byte[] raw_fcgi_header;
            OutputStream resp_s;

            resp_s = sock.getOutputStream();
            
            req_fcgi_header.contentLength = response.length;
            req_fcgi_header.type = 6;
            raw_fcgi_header = req_fcgi_header.deconstruct();

            resp_s.write(raw_fcgi_header);
            resp_s.write(response);                        
        }

        public static void handleFCGIRequest(Socket sock) throws java.io.IOException {
            FCGIHeader fcgi_header = new FCGIHeader();

            readRequest(sock, fcgi_header);

            String cont = "<html><title>My FastCGI App</title><body>My FastCGI Application!</body></html>\r\n\r\n";
            String resp = "HTTP/1.0 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + 
                String.valueOf(cont.length()) + "\r\nConnection: close\r\n\r\n" + cont;

            writeResponse(sock, fcgi_header, resp.getBytes());
        }
    }


    public static void main(final String[] argv) throws java.io.IOException {
        byte[] addr = {127, 0, 0, 1};
        ServerSocket svr = new ServerSocket(3000, 5, InetAddress.getByAddress(addr));


        while (true) {
            Socket s = svr.accept();
            FCGI.handleFCGIRequest(s);
            s.close();

            System.out.println("I was called");
        }
    }
}