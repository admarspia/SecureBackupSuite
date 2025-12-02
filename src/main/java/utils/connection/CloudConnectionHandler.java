package utils.connection;


import config.user_config.storage_config.StorageConfigModel;
public class CloudConnectionHandler implements ConnectionHandler {
     @Override
     public boolean test(StorageConfigModel config){
         return false;
     }
}
