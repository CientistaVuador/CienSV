package cien.server.data;

import cien.server.Util;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Field extends Element {

    String value = "";
    public Field(String name) {
        super(name, Types.FIELD);
    }
    
    public Field(String name, String value) {
        super(name, Types.FIELD);
        this.value = value;
    }
    
    protected Field() {
        super(null, Types.FIELD);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public byte[] getValueAsBytes() {
        try {
            return this.value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public byte[] toByteArray() {
        //Type of the field
        byte[] type = {this.type};
        //Value
        byte[] value = getValueAsBytes();
        //Name
        byte[] name = getNameBytes();
        //Value size
        byte[] valueSize = ByteBuffer.allocate(4).putInt(value.length).array();
        //Name size
        byte[] nameSize = ByteBuffer.allocate(4).putInt(name.length).array();
        //Total Size
        byte[] totalSize = ByteBuffer.allocate(4).putInt(value.length+name.length+valueSize.length+nameSize.length).array();
        
        //Return all
        return Util.mixByteArrays(type, totalSize, nameSize, name, valueSize, value);
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
        
        //Value
        byte[] valueSize = new byte[4];
        for (int i = 0; i < 4; i++) {
            valueSize[i] = bytes[currentIndex];
            currentIndex++;
        }
        int valueSizeInt = ByteBuffer.wrap(valueSize).getInt();
        byte[] value = new byte[valueSizeInt];
        for (int i = 0; i < valueSizeInt; i++) {
            value[i] = bytes[currentIndex];
            currentIndex++;
        }
        try {
            this.value = new String(value, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return this.name+": \""+this.value+"\"";
    }
    
    

}
