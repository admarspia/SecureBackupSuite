package utils.connection;

import config.user_config.storage_config.StorageConfigModel;

public class ConnectionHandlerResolver {

    public static ConnectionHandler getHandler(StorageConfigModel config) {
        switch (config.getType()) {
            case SMB:
                return new SMBConnectionHandler();
            case SFTP:
                return new SFTPConnectionHandler();
            case NFS:
                return new NFSConnectionHandler();
            case CLOUD:
                return new CloudConnectionHandler();
            default:
                throw new UnsupportedOperationException(
                        "Unsupported Operation: " + config.getType()
                );
        }
    }
}

