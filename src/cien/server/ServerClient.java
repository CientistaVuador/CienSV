package cien.server;

import java.io.IOException;
import java.net.Socket;

public abstract class ServerClient extends AbstractClient {
    
    /**
     * Creates a new ServerClient
     * @param s the java socket to be wrapped
     * @throws IOException if any I/O error occurs
     */
    public ServerClient(Socket s) throws IOException {
        super(s);
    }
    
}
