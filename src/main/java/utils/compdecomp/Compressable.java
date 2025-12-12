package utils.compdecomp;

import java.nio.file.Path;
import java.io.IOException;

public interface Compressable {
    Path compress(Path source, Path targetDir) throws IOException;
    Path decompress(Path compressedFile, Path targetDir) throws IOException;

}

