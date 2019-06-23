package test;

import cien.server.Logger;
import cien.server.Packet;
import cien.server.Server;
import cien.server.ServerClient;
import cien.server.ServerPacketListener;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TestServer {

    public static void main(String[] args) throws Exception {
        Logger.setLoggingEnabled(true);
        
        Server s = new Server(new ServerSocket(2555));
        ServerPacketListener listener = new ServerPacketListener() {
            @Override
            public void onPacketReceived(ServerClient c, Packet p) {
                if (Arrays.equals(TestPacketID.TIME, p.getId())) {
                    c.sendPacket(new Packet(
                            TestPacketID.TIME,
                            ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array())
                    );
                }
            }
        };
        s.addListener(listener);
        s.start();
    }

}
