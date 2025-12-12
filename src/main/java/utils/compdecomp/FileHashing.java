package utils.compdecomp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public final class FileHashing {
    public static String sha256(Path file) throws IOException , NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(Files.readAllBytes(file));
        return bytesToHex(md.digest());
    }

    private static String bytesToHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}

