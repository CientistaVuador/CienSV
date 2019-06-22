package test;

import cien.server.Logger;
import cien.server.Server;
import java.net.ServerSocket;

public class TestServer {

    public static void main(String[] args) throws Exception {
        Logger.setLoggingEnabled(true);
        
        Server s = new Server(new ServerSocket(2555));
        s.start();
    }

}
