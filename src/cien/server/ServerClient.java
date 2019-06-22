package cien.server;

import java.io.IOException;
import java.net.Socket;

public abstract class ServerClient extends AbstractClient {
    
    public ServerClient(Socket s) throws IOException {
        super(s);
    }
    
}
