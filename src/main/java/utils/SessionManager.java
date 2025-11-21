package utils;

import java.io.*;

public final class SessionManager{ // make all the methods static bc i need to shure them all over the project and creating instatnce doesn make sense 
    private static String PATH = "session_storage.txt";

    public static void startSession(String username) throws IOException {
        try ( FileWriter fw = new FileWriter(PATH) ){
            fw.write(username + System.lineSeparator());

        }

    }

    public static boolean isLoggedIn(String username) throws IOException {
        String name ;
        try (BufferedReader br = new BufferedReader( new FileReader(PATH)) ) {  
            name = br.readLine();
        }

        if (name == null || !name.equals(username)) {
            return false;
        }

        return true; 
    }

    public static String who() throws FileNotFoundException, IOException {
        String name;
        try (BufferedReader br = new BufferedReader( new FileReader(PATH)) ) {
            name = br.readLine();

        }

        return name; 

    }

    public static void endSession() throws IOException {
        try ( FileWriter fw = new FileWriter(PATH) ) {
            fw.write("");// because append is false by default this will clear the line
        }
    }
}
