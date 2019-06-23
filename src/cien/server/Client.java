package cien.server;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client extends AbstractClient {

    private final List<ClientPacketListener> listener = new ArrayList<>(0);
    
    /**
     * Creates a new client
     * @param s the java socket
     * @throws IOException if any I/O Error occurs
     */
    public Client(Socket s) throws IOException {
        super(s);
    }

    /**
     * 
     * @param p
     */
    public void addListener(ClientPacketListener p) {
        listener.add(p);
    }
    
    /**
     *
     * @param p
     */
    public void removeListener(ClientPacketListener p) {
        listener.remove(p);
    }
    
    @Override
    public void onPacketReceived(Packet p) {
        for (ClientPacketListener f:listener.toArray(new ClientPacketListener[listener.size()])) {
            f.onPacketReceived(p);
        }
    }

}
