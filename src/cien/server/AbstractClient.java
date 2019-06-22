package cien.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

public abstract class AbstractClient extends Thread {

    private final Socket s;
    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    
    public AbstractClient(Socket s) throws IOException {
        super(s.toString());
        this.s = s;
        in = new BufferedInputStream(s.getInputStream());
        out = new BufferedOutputStream(s.getOutputStream());
        if (Logger.canLog()) {
            Logger.log("Client "+s+" Created!");
        }
    }

    public Socket getSocket() {
        return s;
    }
    
    @Override
    public synchronized void start() {
        super.start();
        if (Logger.canLog()) {
            Logger.log("Client Thread "+s+" Started!");
        }
    }

    @Override
    public void run() {
        try {
            while (!s.isClosed()) {
                if (Logger.canLog()) {
                    Logger.log(s+" Waiting for packet...");
                }
                //Id Lenght
                byte[] idLenght = new byte[2];
                for (int i = 0; i < 2; i++) {
                    idLenght[i] = (byte) in.read();
                }
                short idLen = ByteBuffer.wrap(idLenght).getShort();
                
                //Id
                byte[] id = new byte[idLen];
                for (int i = 0; i < idLen; i++) {
                    id[i] = (byte) in.read();
                }
                
                //Bytes Size
                byte[] bytesSize = new byte[4];
                for (int i = 0; i < 4; i++) {
                    bytesSize[i] = (byte) in.read();
                }
                int bytesLen = ByteBuffer.wrap(bytesSize).getInt();
                
                //If Packet is Empty
                if (bytesLen<=0) {
                    if (Logger.canLog()) {
                        Logger.log(s+" Empty Packet Received -> ID: "+Util.convertIDToString(id));
                    }
                    onPacketReceived(new Packet(id, new byte[0]));
                    continue;
                }
                
                //Bytes
                byte[] bytes = new byte[bytesLen];
                for (int i = 0; i < bytesLen; i++) {
                    bytes[i] = (byte) in.read();
                }
                
                if (Logger.canLog()) {
                    Logger.log(s+" Packet Received -> ID: "+Util.convertIDToString(id)+" Size: "+(bytesLen+idLen+6));
                }
                onPacketReceived(new Packet(id, bytes));
            }
        } catch (IOException ex) {
            //Todo
        }
    }
    
    public void sendPacket(Packet p) {
        try {
            out.write(p.getAllBytes());
            out.flush();
            if (Logger.canLog()) {
                Logger.log(s+" Sent Packet "+p);
            }
        } catch (IOException ex) {
            if (Logger.canLog()) {
                Logger.log(s+" Failed to send packet: "+p.toString()+" Error -> "+ex.getMessage());
            } else {
                throw new RuntimeException(s+" Failed to send packet: "+p.toString()+" Error -> "+ex.getMessage());
            }
        }
    }
    
    public abstract void onPacketReceived(Packet p);
    
}
