package cien.server;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class Client extends AbstractClient {

    List<ClientPacketListener> listener = new ArrayList<>();
    
    public Client(Socket s) throws IOException {
        super(s);
    }

    public void addListener(ClientPacketListener p) {
        listener.add(p);
    }
    
    public void removeListener(ClientPacketListener p) {
        listener.remove(p);
    }
    
    @Override
    public void onPacketReceived(Packet p) {
        for (ClientPacketListener f:listener.toArray(new ClientPacketListener[0])) {
            f.onPacketReceived(p);
        }
    }

}
