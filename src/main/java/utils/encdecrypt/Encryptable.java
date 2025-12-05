package utils.encdecrypt;

import java.nio.file.Path;
import java.io.IOException;

public interface Encryptable {
    Path encryptFile(Path sourceFile, Path destDir) throws Exception;
    Path decryptFile(Path encryptedFile, Path destDir) throws Exception;
}

