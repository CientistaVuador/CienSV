package test;

import cien.server.BufferedClientPacketListener;
import cien.server.Client;
import cien.server.ClientPacketListener;
import cien.server.Logger;
import cien.server.Packet;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TestBufferedPacketListener {
    
    public static void main(String[] args) throws Exception {
        Client client = new Client(new Socket(InetAddress.getLocalHost(), 2555));
        BufferedClientPacketListener listener = new BufferedClientPacketListener();
        client.addListener(listener);
        client.start();
        
        client.sendPacket(new Packet(TestPacketID.TIME, new byte[0]));
        Packet r = listener.read();
        long time = ByteBuffer.wrap(r.getBytes()).getLong();
        System.out.println("Received Time From Server: "+time);
    }
}
