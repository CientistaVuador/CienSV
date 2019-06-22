package cien.server;

import java.util.*;

public class BufferedClientPacketListener implements ClientPacketListener {

    final List<Packet> buff = new ArrayList<>();
    
    public BufferedClientPacketListener() {
        
    }
    
    @Override
    public void onPacketReceived(Packet p) {
        synchronized (buff) {
            buff.add(p);
            buff.notifyAll();
        }
    }
    
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
    
    public int size() {
        return buff.size();
    }
    
    public boolean isEmpty() {
        return buff.isEmpty();
    }
    
    public synchronized Packet[] clear() {
        Packet[] f = buff.toArray(new Packet[0]);
        buff.clear();
        return f;
    }

}
