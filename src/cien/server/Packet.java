package cien.server;

import java.nio.ByteBuffer;

public class Packet {

    private final byte[] id;
    private final byte[] bytes;
    private final byte[] allBytes;
    
    public Packet(byte[] id, byte[] bytes) {
        if (id==null || bytes==null) 
            throw new NullPointerException("ID or Bytes can't be null");
        if (id.length==0) 
            throw new RuntimeException("ID Can't be empty.");
        if (id.length>=Short.MAX_VALUE) 
            throw new RuntimeException("ID Lenght can't be bigger than Short#MAX_VALUE");
        this.id = id;
        this.bytes = bytes;
        this.allBytes = Util.mixByteArrays(
                ByteBuffer.allocate(2).putShort((short) bytes.length).array(),
                id,
                ByteBuffer.allocate(4).putInt(id.length).array(),
                bytes
        );
        if (Logger.canLog()) {
            Logger.log("Packet \""+Util.convertIDToString(id)+"\" Created, "+allBytes.length+" Bytes");
        }
    }

    public byte[] getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getAllBytes() {
        return allBytes;
    }

    public int getBytesSize() {
        return bytes.length;
    }

    public int getTotalSize() {
        return allBytes.length;
    }
    
    public int getIdLenght() {
        return id.length;
    }

    public boolean isEmpty() {
        return bytes.length<=0;
    }
    
    @Override
    public String toString() {
        return "\""+Util.convertIDToString(id)+"\", "+allBytes.length+" Bytes";
    }
    
    
}
