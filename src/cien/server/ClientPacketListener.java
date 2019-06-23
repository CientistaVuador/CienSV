package cien.server;

public interface ClientPacketListener {

    /**
     * Called when a packet is received
     * @param p the packet received
     */
    public void onPacketReceived(Packet p);
}
