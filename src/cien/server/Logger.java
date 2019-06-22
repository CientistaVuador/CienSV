package cien.server;

import java.io.PrintStream;

public class Logger {
    private Logger() {
        
    }
    
    private static PrintStream stream = System.out;
    private static boolean log = false;
    public static void log(String s) {
        stream.println(s);
    }

    public static void setLoggingEnabled(boolean log) {
        Logger.log = log;
    }

    public static void setStream(PrintStream stream) {
        Logger.stream = stream;
    }

    public static boolean canLog() {
        return log;
    }

    public static PrintStream getLogStream() {
        return stream;
    }
    
}
