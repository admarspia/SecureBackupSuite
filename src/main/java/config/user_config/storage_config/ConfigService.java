package config.user_config.storage_config;

import config.user_config.*;
import utils.connection.ConnectionTester;
import exception.connection.ConnectionTestException;

public class ConfigService { 
    private static StorageConfigModel config;
    private static RootConfig root;

    static {
        try {
            UserConfigLoader loader = new UserConfigLoader();
            root = loader.load();
            config = root.getStorage();

            ConfigValidator.validate(config);

        } catch (Exception ex) {
            System.err.println("ConfigService failed to initialize: " + ex);
            config = null;
        }
    }

    public static boolean testConnection() throws ConnectionTestException {
        if (config == null) return false; 
            ConnectionTester tester = new ConnectionTester(config);
        return tester.auth(); 
    }
    
    public static StorageConfigModel.Type getDestinationType(){
        return config.getType();
    }

    public static String getLocalDest(){
        return config.getPath();
    }

}

