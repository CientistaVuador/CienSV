package cien.server.data;

import cien.server.Util;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

public class Group extends Element {

    final List<Element> elements = new ArrayList<>();
    
    public Group(String name) {
        super(name, Types.GROUP);
    }
    
    protected Group() {
        super(null, Types.GROUP);
    }
    
    public Group add(Element e) {
        if (e.parent!=null) {
            e.parent.remove(e);
            e.parent = this;
        }
        elements.add(e);
        return this;
    }
    
    public boolean remove(Element e) {
        if (elements.remove(e)) {
            e.parent = null;
            return true;
        }
        return false;
    }
    
    public Element getElement(int index) {
        return elements.get(index);
    }
    
    public Element getElement(String name) {
        for (Element e:elements) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
    
    public int size() {
        return elements.size();
    }
    
    public Element[] getElements() {
        return elements.toArray(new Element[elements.size()]);
    }
    
    @Override
    public byte[] toByteArray() {
        //Type of the field
        byte[] type = {this.type};
        //Name
        byte[] name = getNameBytes();
        //Name size
        byte[] nameSize = ByteBuffer.allocate(4).putInt(name.length).array();
        
        //Elements
        byte[][] elements = new byte[this.elements.size()][];
        for (int i = 0; i < this.elements.size(); i++) {
            elements[i] = this.elements.get(i).toByteArray();
        }
        byte[] elementsMixed = Util.mixByteArrays(elements);
        
        //Total Size
        byte[] totalSize = ByteBuffer.allocate(4).putInt(name.length+nameSize.length+elementsMixed.length).array();
        return Util.mixByteArrays(type, totalSize, nameSize, name, elementsMixed);
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
        
        if (currentIndex>=bytes.length) {
            return;
        }
        
        while (currentIndex<bytes.length) {
            byte type = bytes[currentIndex];
            currentIndex++;
            byte[] totalSize = new byte[4];
            for (int i = 0; i < 4; i++) {
                totalSize[i] = bytes[currentIndex];
                currentIndex++;
            }
            int total = ByteBuffer.wrap(totalSize).getInt();
            byte[] allbytes = new byte[total];
            for (int i = 0; i < total; i++) {
                allbytes[i] = bytes[currentIndex];
                currentIndex++;
            }
            byte[] typeArray = {type};
            allbytes = Util.mixByteArrays(typeArray, totalSize, allbytes);
            
            switch (type) {
                case Types.FIELD:
                    Field f = new Field();
                    f.loadFromByteArray(allbytes);
                    add(f);
                    break;
                case Types.GROUP:
                    Group g = new Group();
                    g.loadFromByteArray(allbytes);
                    add(g);
                    break;
                case Types.BYTES_FIELD:
                    BytesField h = new BytesField();
                    h.loadFromByteArray(allbytes);
                    add(h);
                    break;
                case Types.LIST_FIELD:
                    ListField l = new ListField();
                    l.loadFromByteArray(allbytes);
                    add(l);
            }
        }
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int i) {
        StringBuilder b = new StringBuilder();
        b.append(cien.server.data.Util.generateIdentation(i));
        b.append(this.name);
        b.append(":");
        b.append(System.lineSeparator());
        for (Element e:elements) {
            if (e instanceof Group) {
                Group f = ((Group)e);
                b.append(f.toString(i+1));
            } else if (e != null) {
                b.append(cien.server.data.Util.generateIdentation(i+1));
                b.append(e.toString());
                b.append(System.lineSeparator());
            }
        }
        return b.toString();
    }
    
    
    
    
    
}
