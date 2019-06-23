package cien.server;

import java.nio.ByteBuffer;

public class Packet {

    private final byte[] id;
    private final byte[] bytes;
    private final byte[] allBytes;
    
    /**
     * Creates a new packet.
     * 
     * @param id A Byte Array as the identification
     * @param bytes The bytes of the packet
     * @throws NullPointerException if id or bytes is null
     * @throws RuntimeException if id lenght is 0 or bigger than Short#MAX_VALUE
     */
    public Packet(byte[] id, byte[] bytes) {
        if (id==null || bytes==null) {
            throw new NullPointerException("ID or Bytes can't be null");
        }
        if (id.length==0) {
            throw new RuntimeException("ID Can't be empty.");
        }
        if (id.length>=Short.MAX_VALUE) {
            throw new RuntimeException("ID Lenght can't be bigger than Short#MAX_VALUE");
        }
        this.id = id;
        this.bytes = bytes;
        this.allBytes = Util.mixByteArrays(
                ByteBuffer.allocate(2).putShort((short) id.length).array(),
                id,
                ByteBuffer.allocate(4).putInt(bytes.length).array(),
                bytes
        );
    }

    /**
     *
     * @return the id byte array
     */
    public byte[] getId() {
        return id;
    }

    /**
     *
     * @return the bytes of the packet
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Returns all bytes of the packet,
     * @return all bytes of the packet
     */
    public byte[] getAllBytes() {
        return allBytes;
    }

    /**
     * Same as getBytes().lenght
     * @return
     */
    public int getBytesSize() {
        return bytes.length;
    }

    /**
     * Same as getAllBytes().lenght
     * 
     * The total size will be
     * bytes lenght + id lenght + 6 bytes
     * 
     * @return
     */
    public int getTotalSize() {
        return allBytes.length;
    }
    
    /**
     * Same as getId().lenght
     * @return
     */
    public int getIdLenght() {
        return id.length;
    }

    /**
     * Returns true if the packet has no bytes
     * @return true if the packet has no bytes
     */
    public boolean isEmpty() {
        return bytes.length<=0;
    }
    
    @Override
    public String toString() {
        return "\""+Util.convertIDToString(id)+"\", "+allBytes.length+" Bytes";
    }
    
    
}
