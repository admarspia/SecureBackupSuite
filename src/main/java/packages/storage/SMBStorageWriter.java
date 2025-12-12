package storage;



import config.user_config.storage_config.StorageConfigModel;
import java.nio.file.Path;
import java.io.IOException;

public class SMBStorageWriter implements StorageWriter {

    @Override
    public void write(StorageConfigModel config, Path file) throws IOException {
    }
}

