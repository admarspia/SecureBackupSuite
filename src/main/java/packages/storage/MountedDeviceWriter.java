package storage;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import config.user_config.storage_config.StorageConfigModel;

public class MountedDeviceWriter implements StorageWriter{
    public void write(StorageConfigModel config, Path encrypted) throws IOException {
        Path destDir = Path.of(config.getMountPoint());

        Files.createDirectories(destDir);

        Path destFile = destDir.resolve(encrypted.getFileName());

        try (var in = Files.newInputStream(encrypted);
             var out = Files.newOutputStream(destFile)) {
            in.transferTo(out);
            System.out.println("Written: " + destFile);
        }
    }
    
}
