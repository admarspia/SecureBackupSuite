package utils;
import org.mindrot.jbcrypt.BCrypt;

public class EncryptionUtils{
    public static String hashPassword(String rawData){
        return BCrypt.hashpw(rawData, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String enterdPassword, String storedHash){
        return BCrypt.checkpw(enterdPassword, storedHash);
    }
}


