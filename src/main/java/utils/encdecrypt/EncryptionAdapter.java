package utils.encdecrypt;

import java.nio.file.Path;
import java.util.Objects;



public final class EncryptionAdapter {
    private static volatile Encryptable handler;

    private EncryptionAdapter() {}

    public static void setHandler(Encryptable h) {
        handler = Objects.requireNonNull(h);
    }

    public static Path encrypt(Path compressedFile) throws Exception {
        if (handler == null) throw new IllegalStateException("Encryption handler not configured");
        return handler.encryptFile(compressedFile, compressedFile.getParent());
    }

    public static Path decrypt(Path encryptedFile) throws Exception {
        if (handler == null) throw new IllegalStateException("Encryption handler not configured");
        return handler.decryptFile(encryptedFile, encryptedFile.getParent());
    }
}

