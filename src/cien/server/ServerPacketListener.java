package cien.server;

public interface ServerPacketListener {

    /**
     * Called when a packet is received from a client
     * @param c the client
     * @param p the packet
     */
    public void onPacketReceived(ServerClient c, Packet p);
}
