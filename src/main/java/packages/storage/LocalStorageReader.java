package storage;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.io.IOException;

import config.user_config.storage_config.StorageConfigModel;
import utils.Queues;

public class LocalStorageReader implements StorageReader {

    @Override
    public void read(StorageConfigModel config, Path downloadDir) throws IOException, InterruptedException {
        Path sourceDir = Path.of(config.getPath());
        if (!Files.exists(sourceDir) || !Files.isDirectory(sourceDir)) {
            throw new IOException("Local backup directory does not exist: " + sourceDir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir, "*.enc")) {
            for (Path file : stream) {
                Path target = downloadDir.resolve(file.getFileName());
                Files.copy(file, target);
                Queues.ENCRYPTED_QUEUE.put(target);
                System.out.println("Local file queued: " + target.getFileName());
            }
        }

        Queues.ENCRYPTED_QUEUE.put(Queues.POISON);
    }
}

