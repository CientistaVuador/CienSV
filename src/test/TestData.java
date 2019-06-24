package test;

import cien.server.*;
import cien.server.data.BytesField;
import cien.server.data.Element;
import cien.server.data.Field;
import cien.server.data.Group;
import cien.server.data.Util;
import java.io.File;
import java.io.IOException;

public class TestData {

    public static void main(String[] args) throws Exception {
        
    }
    
    public static void testFields() {
        Field f = new Field("MyField~~", "MyValue~~");
        System.out.println(f.toString());
        byte[] gg = f.toByteArray();
        Field h = new Field(null);
        h.loadFromByteArray(gg);
        System.out.println(h.toString());
    }

    public static void testGroupToString() {
        Group g = new Group("MyGroup~");
        Field h = new Field("MyField~", "MyValue~");
        g.add(h);
        Group a = new Group("MySubGroup~");
        Field k = new Field("MyAnotherField~", "MyAnotherValue~");
        a.add(k);
        g.add(a);
        
        System.out.println(g.toString());
    }
    
    public static void testGroup() {
        Group g = new Group("MyGroup~");
        Field h = new Field("MyField~", "MyValue~");
        g.add(h);
        Group a = new Group("MySubGroup~");
        Field k = new Field("MyAnotherField~", "MyAnotherValue~");
        a.add(k);
        g.add(a);
        
        System.out.println(g.toString());
        
        byte[] bytes = g.toByteArray();
        
        Group t = new Group(null);
        t.loadFromByteArray(bytes);
        
        System.out.println(t.toString());
    }
    
    public static void testBytesField() {
        byte[] testBytes = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        BytesField field = new BytesField("MyField");
        field.write(testBytes);
        
        System.out.println(field);
        
        byte[] fieldBytes = field.toByteArray();
        
        BytesField other = new BytesField(null);
        
        other.loadFromByteArray(fieldBytes);
        
        System.out.println(other);
        
        for (byte b:other.getBytes()) {
            System.out.println(b);
        }
    }
    
    public static void testAll() {
        byte[] testBytes = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        
        Group group = new Group("Group")
                .add(new Field("MyField", "Value"))
                .add(new BytesField("BytesField", testBytes))
                .add(new Group("SubGroup")
                        .add(new Field("MyField", "Value"))
                        .add(new BytesField("BytesField", testBytes))
                );
        
        System.out.println(group);
        
        byte[] bytes = group.toByteArray();
        
        Group copy = new Group(null);
        copy.loadFromByteArray(bytes);
        
        System.out.println(copy);
    }
    
    public static void testGroupsAndPackets() {
        byte[] testBytes = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        
        Group group = new Group("Group")
                .add(new Field("MyField", "Value"))
                .add(new BytesField("BytesField", testBytes))
                .add(new Group("SubGroup")
                        .add(new Field("MyField", "Value"))
                        .add(new BytesField("BytesField", testBytes))
                );
        System.out.println(group);
        
        Packet p = Util.packetFromGroup(TestPacketID.TIME, group);
        
        Group t = Util.groupFromPacket(p);
        
        System.out.println(t);
    }
    
}
