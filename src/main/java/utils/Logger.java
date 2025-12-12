package utils;

import java.io.*;
import java.time.*;
public class Logger {
    private static String PATH = "backup_system.log";
    private static String now = LocalTime.now().toString();

    public static void log(String eventType, String source, String message ) {
        String logMsg = eventType + "|" + source + "|" + message + "|" + now + System.lineSeparator();

        try ( FileWriter fw = new FileWriter(PATH, true)) {
            fw.write(logMsg);
            System.out.println("written to log.");

        } catch (IOException ex){
           System.out.println("Error: " + ex);

        }
    }
}
