package cien.server;

import java.io.PrintStream;

public class Logger {
    
    
    private static PrintStream stream = System.out;
    private static boolean log = false;
    
    /**
     * Logs a message to the printstream of this logger
     * @param s the message
     */
    public static void log(String s) {
        if (log) {
            stream.println(s);
        }
    }

    /**
     * Activates or deactivates logging
     * @param log
     */
    public static void setLoggingEnabled(boolean log) {
        Logger.log = log;
    }

    /**
     * Sets the printstream to be used
     * @param stream
     */
    public static void setStream(PrintStream stream) {
        Logger.stream = stream;
    }

    /**
     * Returns true if logging is enabled
     * @return
     */
    public static boolean canLog() {
        return log;
    }

    /**
     * Returns the printstream being used by this class
     * @return
     */
    public static PrintStream getLogStream() {
        return stream;
    }
    
    private Logger() {
        
    }
    
    
}
