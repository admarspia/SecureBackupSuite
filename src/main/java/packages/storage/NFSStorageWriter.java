package storage;



import config.user_config.storage_config.StorageConfigModel;
import java.nio.file.Path;
import java.io.IOException;

public class NFSStorageWriter implements StorageWriter {
    // implementetion on version 2 
    @Override
    public void write(StorageConfigModel config, Path file) throws IOException {
    }
}

