package backup;

import java.nio.file.Path;
import java.util.Set;

import config.user_config.file_config.BackupFilesConfigModel;
import config.user_config.file_config.ConfigService; 
import config.user_config.storage_config.StorageConfigModel;
import config.user_config.schedule_config.BackupScheduleConfigModel;

import utils.compdecomp.GzipCompressor;
import utils.compdecomp.CompressionManager;
import utils.connection.*;
import utils.connection.helpers.*;
import utils.encdecrypt.*;
import storage.*;
import utils.Logger;
import utils.compdecomp.FileUtils;

public class FullBackupService implements Backupable {

    @Override
    public void backup() throws Exception {
        try {
            BackupFilesConfigModel fileConfig =
                config.user_config.file_config.ConfigService.getConfig();

            Set<String> srcDirs = fileConfig.getSourcePaths();
            Set<String> includePatterns = fileConfig.getIncludePatterns();
            boolean recursive = fileConfig.getRecursive();

            StorageConfigModel conf =
                config.user_config.storage_config.ConfigService.getConfig();
        
            StorageConfigModel.Type type = conf.getType();

            String base64Key = conf.getEncryptionKey();
            EncryptionHandler encryptionHandler = EncryptionHandler.fromBase64Key(base64Key);
            EncryptionAdapter.setHandler(encryptionHandler);

            CompressionManager compressor = new CompressionManager(
                    new GzipCompressor(),
                    Path.of("backup_workspace/temp/compressed/"),
                    2,
                    BackupModel.Type.FULL
                    );

            EncrypterService encrypter = new EncrypterService(2);
            StorageManager storageManager = null;

            switch (type){
                case LOCAL:
                    storageManager = new StorageManager(
                            new LocalStorageWriter(),
                            conf,
                            2
                            );
                    break;
                case EXTERNAL:
                case PARTITION: 
                    storageManager = new StorageManager(
                            new MountedDeviceWriter(),
                            conf,
                            2
                            );
                    break;
                case SMB:
                    storageManager = new StorageManager(
                            new SMBStorageWriter(),
                            conf,
                            2
                            );
                    break;
                case SFTP:
                    System.out.println("inside sftp writer");
                    System.out.println(config.user_config.storage_config.ConfigService.testConnection());
                    storageManager = new StorageManager(
                            new SFTPStorageWriter(),
                            conf,
                            2);
                    break;
                default:
                    throw new UnsupportedOperationException(
                        "Unsupported Operation: " + type);

            }

            encrypter.start();
            storageManager.start();

            compressor.compressAll(srcDirs, includePatterns, recursive);


            encrypter.stop();
            storageManager.stop();

            System.out.println("Full backup completed.");
            Logger.log(BackupScheduleConfigModel.Status.SUCCESS.name(), "backup", "backup completed successfully.");

            FileUtils.cleanup(Path.of("backup_workspace/temp/compressed"));

        } catch (Exception ex) {
            throw ex;
        }
    }

}

