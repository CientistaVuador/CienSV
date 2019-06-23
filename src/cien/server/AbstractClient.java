package cien.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class AbstractClient extends Thread {

    private final Socket s;
    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    private final HashMap<String, Object> map = new HashMap<>();
    
    /**
     * Creates a new client
     * @param s The Java Socket to be wrapped
     * @throws IOException if an I/O error occurs
     */
    public AbstractClient(Socket s) throws IOException {
        super(s.toString());
        this.s = s;
        in = new BufferedInputStream(s.getInputStream());
        out = new BufferedOutputStream(s.getOutputStream());
        if (Logger.canLog()) {
            Logger.log("Client "+s+" Created!");
        }
    }

    public void put(String key, Object obj) {
        map.put(key, obj);
    }
    
    public Object get(String key) {
        return map.get(key);
    }
    
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(map.get(key));
    }
    
    /**
     * Returns the socket of this client
     * @return The java socket
     */
    public Socket getSocket() {
        return s;
    }
    
    /**
     * Starts the client
     */
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
                    Logger.log(s+" Waiting for packets...");
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
    
    /**
     * Sends a packet to the server.
     * @param p the packet to be send
     */
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
    
    /**
     * Called when this client receives a packet.
     * @param p the packet received.
     */
    public abstract void onPacketReceived(Packet p);
    
}
