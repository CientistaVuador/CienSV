package cien.server.data;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListField extends Element {
    
    final List<String> list = new ArrayList<>(0);
    
    public ListField(String name) {
        super(name, Types.LIST_FIELD);
    }
    
    public ListField(String name, String... values) {
        super(name, Types.LIST_FIELD);
        list.addAll(Arrays.asList(values));
    }
    
    public ListField(String name, Collection<? extends String> col) {
        super(name, Types.LIST_FIELD);
        list.addAll(col);
    }
    
    protected ListField() {
        super(null, Types.LIST_FIELD);
    }
    
    public ListField addAll(Collection<? extends String> col) {
        list.addAll(col);
        return this;
    }
    
    public ListField add(String... s) {
        list.addAll(Arrays.asList(s));
        return this;
    }
    
    public ListField add(String s) {
        list.add(s);
        return this;
    }
    
    public String get(int i) {
        return list.get(i);
    }
    
    public String[] getValues() {
        return list.toArray(new String[list.size()]);
    }
    
    public void remove(int index) {
        list.remove(index);
    }
    
    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(this.name);
        b.append(": [");
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            b.append("\"");
            b.append(s);
            b.append("\"");
            if (i<(list.size()-1)) {
                b.append(',');
            }
        }
        b.append("]");
        return b.toString();
    }
    
    @Override
    public byte[] toByteArray() {
        byte[] type = {Types.LIST_FIELD};
        byte[] name = getNameBytes();
        byte[] nameSize = ByteBuffer.allocate(4).putInt(name.length).array();
        byte[] elements = ByteBuffer.allocate(4).putInt(list.size()).array();
        byte[][] elementsBytes = new byte[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            try {
                byte[] text = list.get(i).getBytes("UTF-8");
                byte[] size = ByteBuffer.allocate(4).putInt(text.length).array();
                elementsBytes[i] = cien.server.Util.mixByteArrays(size, text);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
        byte[] elementsBytesMix = cien.server.Util.mixByteArrays(elementsBytes);
        byte[] totalSize = ByteBuffer.allocate(4).putInt(name.length+nameSize.length+elements.length+elementsBytesMix.length).array();
        return cien.server.Util.mixByteArrays(
                type,
                totalSize,
                nameSize,
                name,
                elements,
                elementsBytesMix
        );
    }

    @Override
    public void loadFromByteArray(final byte[] bytes) {
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
        
        //Values Size
        byte[] values = new byte[4];
        for (int i = 0; i < 4; i++) {
            values[i] = bytes[currentIndex];
            currentIndex++;
        }
        int nofelements = ByteBuffer.wrap(values).getInt();
        
        if (nofelements<=0) {
            return;
        }
        
        //Values
        for (int i = 0; i < nofelements; i++) {
            //Value
            byte[] valueSize = new byte[4];
            for (int k = 0; k < 4; k++) {
                valueSize[k] = bytes[currentIndex];
                currentIndex++;
            }
            int valueSizeInt = ByteBuffer.wrap(valueSize).getInt();
            byte[] value = new byte[valueSizeInt];
            for (int k = 0; k < valueSizeInt; k++) {
                value[k] = bytes[currentIndex];
                currentIndex++;
            }
            try {
                this.list.add(new String(value, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
}
