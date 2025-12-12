package backup;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import config.user_config.file_config.BackupFilesConfigModel;
import config.user_config.file_config.ConfigService;
import config.user_config.storage_config.StorageConfigModel;

import utils.compdecomp.*;
import utils.encdecrypt.*;

import storage.StorageManager;
import storage.LocalStorageWriter;
import storage.MountedDeviceWriter;
import storage.SMBStorageWriter;
import storage.SFTPStorageWriter;
import utils.Logger;
import config.user_config.schedule_config.BackupScheduleConfigModel;


public class IncrementalBackupService implements Backupable {

    private static final Path MANIFEST_PATH = Path.of("backup_workspace/temp/archive/manifest.json");

    @Override
    public void backup() throws Exception {
        try {
            BackupFilesConfigModel fileConfig = ConfigService.getConfig();
            Set<String> srcDirs = fileConfig.getSourcePaths();
            Set<String> includePatterns = fileConfig.getIncludePatterns();
            boolean recursive = fileConfig.getRecursive();

            StorageConfigModel storageConfig = config.user_config.storage_config.ConfigService.getConfig();
            StorageConfigModel.Type storageType = storageConfig.getType();

            String base64Key = storageConfig.getEncryptionKey();
            EncryptionHandler encryptionHandler = EncryptionHandler.fromBase64Key(base64Key);
            EncryptionAdapter.setHandler(encryptionHandler);

            Set<String> backedUpHashes = loadManifestHashes(MANIFEST_PATH);

            CompressionManager compressor = new CompressionManager(
                    new GzipCompressor(),
                    Path.of("backup_workspace/temp/compressed/"),
                    2,
                    BackupModel.Type.INCREMENTAL
            );

            EncrypterService encrypter = new EncrypterService(2);

            StorageManager storageManager = switch (storageType) {
                case LOCAL -> new StorageManager(new LocalStorageWriter(), storageConfig, 2);
                case EXTERNAL, PARTITION -> new StorageManager(new MountedDeviceWriter(), storageConfig, 2);
                case SMB -> new StorageManager(new SMBStorageWriter(), storageConfig, 2);
                case SFTP -> new StorageManager(new SFTPStorageWriter(), storageConfig, 2);
                default -> throw new UnsupportedOperationException("Unsupported Operation: " + storageType);
            };

            Set<Path> filesToBackup = new HashSet<>();
            for (String srcDir : srcDirs) {
                for (String pattern : includePatterns) {
                    List<Path> files = utils.compdecomp.FileUtils.findFiles(Path.of(srcDir), pattern, recursive);
                    for (Path file : files) {
                        String hash = utils.compdecomp.FileHashing.sha256(file);
                        if (!backedUpHashes.contains(hash)) {
                            filesToBackup.add(file);
                        }
                    }
                }
            }

            encrypter.start();
            storageManager.start();

            compressor.compressFiles(filesToBackup);

            encrypter.stop();
            storageManager.stop();

            System.out.println("Incremental backup completed.");
            Logger.log(BackupScheduleConfigModel.Status.SUCCESS.name(), "backup", "Incremental backup completed successfully.");

        } catch (Exception ex) {
            Logger.log(BackupScheduleConfigModel.Status.FAILED.name(), "backup", ex.getMessage());
            throw ex;
        }
    }

    private Set<String> loadManifestHashes(Path manifestPath) {
        Set<String> hashes = new HashSet<>();
        if (!Files.exists(manifestPath)) return hashes;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(manifestPath.toFile());
            JsonNode entries = root.get("entries");
            if (entries != null && entries.isArray()) {
                for (JsonNode entry : entries) {
                    String hash = entry.get("hash").asText();
                    hashes.add(hash);
                }
            }
        } catch (IOException ex) {
            Logger.log("ERROR","Failed to read manifest", ex.getMessage());
            throw ex;
        }

        return hashes;
    }
}

