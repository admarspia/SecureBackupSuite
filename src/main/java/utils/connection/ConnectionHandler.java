package utils.connection;

import config.user_config.storage_config.StorageConfigModel;

public interface ConnectionHandler {
    boolean test(StorageConfigModel config);
}
