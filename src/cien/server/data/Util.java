package cien.server.data;

import cien.server.Packet;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Util {
    
    protected static String generateIdentation(int s) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s; i++) {
            b.append('\t');
        }
        return b.toString();
    }
    
    public static BytesField bytesFieldfromFile(File f) throws FileNotFoundException, IOException {
        if (f.isFile()) {
            BytesField g = new BytesField(f.getName());
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(f))) {
                int r;
                while ((r = in.read()) != -1) {
                    g.write((byte)r);
                }
            }
            return g;
        }
        return null;
    }
    
    public static Field fieldFromTextFile(File f, String encoding) throws FileNotFoundException, IOException {
        if (f.isFile()) {
            StringBuilder b;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding))) {
                b = new StringBuilder();
                int r;
                while ((r = in.read()) != -1) {
                    b.append((char)r);
                }
            }
            Field g = new Field(f.getName(), b.toString());
            return g;
        }
        return null;
    }
    
    public static Packet packetFromGroup(byte[] id, Group p) {
        return new Packet(id, p.toByteArray());
    }
    
    public static Group groupFromPacket(Packet p) {
        Group g = new Group();
        g.loadFromByteArray(p.getBytes());
        return g;
    }
    
    private Util() {
        
    }
}
