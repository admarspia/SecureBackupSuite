package utils;

import java.io.*;

public class Logger {
    private static String PATH = "backup_system.log";

    public static void log(String eventType, String source, String message) {
        String logMsg = eventType + "|" + source + "|" + message + System.lineSeparator();

        try ( FileWriter fw = new FileWriter(PATH, true)) {
            fw.write(logMsg);
            System.out.println("written to log.");

        } catch (IOException ex){
           System.out.println("Error: " + ex);

        }
    }
}
