package cien.server;

public interface ServerPacketListener {
    public void onPacketReceived(ServerClient c, Packet p);
}
