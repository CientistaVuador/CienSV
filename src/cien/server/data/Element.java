package cien.server.data;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Element {

    String name;
    Group parent = null;
    byte type = -1;
    protected Element(String name, byte type) {
        this.name = name;
        this.type = type;
    }
    
    protected Element() {
        this.name = "";
    }

    public byte getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public byte[] getNameBytes() {
        try {
            return this.name.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Group getParent() {
        return parent;
    }
    
    public abstract byte[] toByteArray();
    public abstract void loadFromByteArray(byte[] bytes);
}
