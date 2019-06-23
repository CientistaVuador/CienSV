package test;

import cien.server.Client;
import cien.server.ClientPacketListener;
import cien.server.Logger;
import cien.server.Packet;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TestClient {

    public static void main(String[] args) throws Exception {
        Logger.setLoggingEnabled(true);
        
        Client client = new Client(new Socket(InetAddress.getLocalHost(), 2555));
        ClientPacketListener listener = (Packet p) -> {
            if (Arrays.equals(p.getId(), TestPacketID.TIME)) {
                long time = ByteBuffer.wrap(p.getBytes()).getLong();
                System.out.println("Received Time From Server: "+time);
            }
        };
        client.addListener(listener);
        client.start();
        client.sendPacket(new Packet(TestPacketID.TIME, new byte[0]));
    }

}
