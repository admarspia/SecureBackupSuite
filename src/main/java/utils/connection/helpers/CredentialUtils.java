package utils.connection.helpers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class CredentialUtils {

    public static String readPasswordPath(String path) throws IOException {
        Path p = Path.of(path); 
        return Files.readAllLines(p, StandardCharsets.UTF_8).get(0).trim(); 
    }

    public static boolean fileExists(String path) {
        return path != null && Files.exists(Path.of(path));
    }
}

