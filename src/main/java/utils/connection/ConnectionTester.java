package utils.connection;

import config.user_config.storage_config.StorageConfigModel;
import exception.connection.ConnectionTestException;

public class ConnectionTester {  
    private final StorageConfigModel config;

    public ConnectionTester(StorageConfigModel config){
        this.config = config;
    }

    public boolean auth()  throws ConnectionTestException {
        try {
            ConnectionHandler handler = ConnectionHandlerResolver.getHandler(config);
            if (handler == null)
                return false;
            return handler.test(config);
        } catch (Exception ex) {
            throw new ConnectionTestException("Connection Failed.", ex);
        }
    }
}

