package storage;

import java.io.IOException;
import java.nio.file.Path;

import config.user_config.storage_config.StorageConfigModel;

public interface StorageReader {
    void read(StorageConfigModel config, Path downloadDir) throws IOException, InterruptedException ;
}
