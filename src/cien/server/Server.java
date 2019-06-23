package cien.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {

    List<ServerPacketListener> listener = new ArrayList<>(0);
    
    private final ServerSocket sv;

    /**
     * Creates a new server
     * @param sv the java serversocket to be wrapped
     */
    public Server(ServerSocket sv) {
        super(sv.toString());
        this.sv = sv;
        Logger.log(sv+" Created!");
    }

    /**
     * Returns the serversocket of this server
     * @return
     */
    public ServerSocket getServer() {
        return sv;
    }

    /**
     * Starts the server
     */
    @Override
    public synchronized void start() {
        super.start();
        Logger.log(sv+" Started!");
    }
    
    @Override
    public void run() {
        try {
            Socket socket;
            while ((socket = sv.accept()) != null) {
                Logger.log(sv+" Client Connected! -> "+socket);
                ServerClient client = new ServerClient(socket) {
                    @Override
                    public void onPacketReceived(Packet p) {
                        onServerPacketReceived(p, this);
                    }
                };
                client.start();
                Logger.log(sv+" Waiting for clients...");
            }
        } catch (IOException ex) {
            //Todo
        }
    }
    
    public void addListener(ServerPacketListener l) {
        listener.add(l);
    }
    
    public void removeListener(ServerPacketListener l) {
        listener.remove(l);
    }
    
    /**
     * Called when a packet is received from a client
     * @param p The packet received
     * @param c The client
     */
    public void onServerPacketReceived(Packet p, ServerClient c) {
        for (ServerPacketListener lis:listener.toArray(new ServerPacketListener[0])) {
            lis.onPacketReceived(c, p);
        }
    }
}
