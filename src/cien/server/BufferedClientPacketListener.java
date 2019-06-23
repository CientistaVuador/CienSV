package cien.server;

import java.util.*;

public class BufferedClientPacketListener implements ClientPacketListener {

    final List<Packet> buff = new ArrayList<>();
    
    /**
     * Creates a new buffered listener
     */
    public BufferedClientPacketListener() {
        
    }
    
    @Override
    public void onPacketReceived(Packet p) {
        synchronized (buff) {
            buff.add(p);
            buff.notifyAll();
        }
    }
    
    /**
     * Read a packet
     * If the buffer is empty, it will wait for a packet to arrive
     * @return the packet
     * @throws InterruptedException if the current thread is interrupted.
     */
    public Packet read() throws InterruptedException {
        synchronized (buff) {
            if (buff.isEmpty()) {
                buff.wait();
            }
            Packet p = buff.get(0);
            buff.remove(p);
            return p;
        }
    }
    
    /**
     * Read a packet even if the buffer is empty.
     * @return a packet, or null if the buffer is empty
     */
    public Packet readNow() {
        synchronized (buff) {
            if (buff.isEmpty()) {
                return null;
            } else {
                Packet t = buff.get(0);
                buff.remove(t);
                return t;
            }
        }
    }
    
    /**
     * Returns The buffer size
     * @return The buffer size
     */
    public int size() {
        return buff.size();
    }
    
    /**
     * Returns true if the buffer is empty
     * @return true if the buffer is empty
     */
    public boolean isEmpty() {
        return buff.isEmpty();
    }
    
    /**
     * Clears the buffer.
     * @return the packets that were in the buffer
     */
    public synchronized Packet[] clear() {
        Packet[] f = buff.toArray(new Packet[buff.size()]);
        buff.clear();
        return f;
    }

}
