package cien.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {

    List<ServerPacketListener> listener = new ArrayList<>();
    
    private final ServerSocket sv;
    public Server(ServerSocket sv) {
        super(sv.toString());
        this.sv = sv;
        if (Logger.canLog()) {
            Logger.log(sv+" Created!");
        }
    }

    public ServerSocket getServer() {
        return sv;
    }

    @Override
    public synchronized void start() {
        super.start();
        if (Logger.canLog()) {
            Logger.log(sv+" Started!");
        }
    }
    
    @Override
    public void run() {
        try {
            Socket socket;
            while ((socket = sv.accept()) != null) {
                if (Logger.canLog()) {
                    Logger.log(sv+" Client Connected! -> "+socket);
                }
                ServerClient client = new ServerClient(socket) {
                    @Override
                    public void onPacketReceived(Packet p) {
                        onServerPacketReceived(p, this);
                    }
                };
                client.start();
                if (Logger.canLog()) {
                    Logger.log(sv+" Waiting for clients...");
                }
            }
        } catch (IOException ex) {
            
        }
    }
    
    public void addListener(ServerPacketListener l) {
        listener.add(l);
    }
    
    public void removeListener(ServerPacketListener l) {
        listener.remove(l);
    }
    
    public void onServerPacketReceived(Packet p, ServerClient c) {
        for (ServerPacketListener lis:listener.toArray(new ServerPacketListener[0])) {
            lis.onPacketReceived(c, p);
        }
    }
}
