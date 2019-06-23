package cien.server;
public class Util {

    /**
     * Mix byte arrays into one
     * @param arr
     * @return
     */
    public static byte[] mixByteArrays(final byte[]... arr) {
        int allBytes = 0;
        for (byte[] a:arr) {
            for (byte b:a) {
                allBytes++;
            }
        }
        
        final byte[] all = new byte[allBytes];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                all[index] = arr[i][j];
                index++;
            }
        }
        return all;
    }
    
    protected static String convertIDToString(byte[] id) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length; i++) {
            builder.append(Byte.toString(id[i]));
            if (i<(id.length-1)) {
                builder.append(".");
            }
        }
        return builder.toString();
    }
    
}
