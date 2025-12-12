package backup;

import java.nio.file.Path;
import java.util.Set;

import config.user_config.file_config.BackupFilesConfigModel;
import config.user_config.file_config.ConfigService as FileConfigService;
import config.user_config.storage_config.StorageConfigModel;
import config.user_config.storage_config.ConfigService as StorageConfigService;

import utils.compdecomp.GzipCompressor;
import utils.compdecomp.CompressionManager;

import utils.encdecrypt.EncrypterService;

import storage.LocalStorageWriter;
import storage.StorageManager;

public class FullBackupService implements Backupable {

    @Override
    public void backup() {
        StorageConfigModel.Type storageType = StorageConfigService.getDestinationType();
        BackupFilesConfigModel fileConfig = FileConfigService.getConfig();

        Set<String> srcDirs = fileConfig.getSources();
        Set<String> includePatterns = fileConfig.getIncludePatterns();
        boolean recursive = fileConfig.isRecursive();

        Path localDest;

        switch (storageType) {
            case LOCAL:
                localDest = StorageConfigService.getLocalDest();
                break;

            default:
                throw new UnsupportedOperationException("Only LOCAL storage is implemented");
        }

        CompressionManager compressor = new CompressionManager(
                new GzipCompressor(),
                Path.of("backup_workspace/temp/compressed/"),
                2
        );

        EncrypterService encrypter = new EncrypterService(2);

        StorageManager storageManager = new StorageManager(
                new LocalStorageWriter(),
                localDest,
                2
        );

        encrypter.start();
        storageManager.start();

        compressor.compressAll(srcDirs, includePatterns, recursive);

        compressor.stop();                
        encrypter.stop();         
        storageManager.stop();  

        System.out.println("Full backup completed.");
    }
}

