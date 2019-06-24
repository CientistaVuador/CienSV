package cien.server.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BytesField extends Element {
    
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    public BytesField(String name) {
        super(name, Types.BYTES_FIELD);
    }
    
    public BytesField(String name, byte[] bytes) {
        super(name, Types.BYTES_FIELD);
        try {
            this.out.write(bytes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    protected BytesField() {
        super(null, Types.BYTES_FIELD);
    }
    
    
    public ByteArrayOutputStream getStream() {
        return out;
    }
    
    public void write(byte b) {
        out.write(b);
    }
    
    public void write(byte[] bytes) {
        try {
            out.write(bytes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public byte[] getBytes() {
        return out.toByteArray();
    }
    
    @Override
    public byte[] toByteArray() {
        //Type of the field
        byte[] type = {this.type};
        //Name
        byte[] name = getNameBytes();
        //Name size
        byte[] nameSize = ByteBuffer.allocate(4).putInt(name.length).array();
        //Byte
        byte[] bytes = getBytes();
        //Bytes Size
        byte[] bytesSize = ByteBuffer.allocate(4).putInt(bytes.length).array();
        //Total Size
        byte[] totalSize = ByteBuffer.allocate(4).putInt(name.length+nameSize.length+bytes.length+bytesSize.length).array();
        
        return cien.server.Util.mixByteArrays(type, totalSize, nameSize, name, bytesSize, bytes);
    }

    @Override
    public void loadFromByteArray(byte[] bytes) {
        //Name
        int currentIndex = 5;
        byte[] nameSize = new byte[4];
        for (int i = 0; i < 4; i++) {
            nameSize[i] = bytes[currentIndex];
            currentIndex++;
        }
        int nameSizeInt = ByteBuffer.wrap(nameSize).getInt();
        byte[] name = new byte[nameSizeInt];
        for (int i = 0; i < nameSizeInt; i++) {
            name[i] = bytes[currentIndex];
            currentIndex++;
        }
        try {
            this.name = new String(name, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        
        //Bytes
        byte[] bytesSize = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytesSize[i] = bytes[currentIndex];
            currentIndex++;
        }
        int bytesSizeInt = ByteBuffer.wrap(bytesSize).getInt();
        if (bytesSizeInt==0) {
            return;
        }
        byte[] bytess = new byte[bytesSizeInt];
        for (int i = 0; i < bytesSizeInt; i++) {
            bytess[i] = bytes[currentIndex];
            currentIndex++;
        }
        try {
            this.out.write(bytess);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return this.name+" -> "+out.size()+" bytes";
    }

    
    
}
