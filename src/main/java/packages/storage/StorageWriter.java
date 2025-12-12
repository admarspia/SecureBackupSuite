package storage;

import java.nio.file.Path;
import java.io.IOException;
import config.user_config.storage_config.StorageConfigModel;

public interface StorageWriter{
    void write(StorageConfigModel Location, Path path) throws IOException;
}
