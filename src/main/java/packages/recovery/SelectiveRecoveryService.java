package recovery;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.compdecomp.FileUtils;
import utils.compdecomp.Compressable;
import utils.compdecomp.GzipCompressor;
import utils.encdecrypt.EncryptionAdapter;
import utils.encdecrypt.EncryptionHandler;
import utils.manifest.ManifestEntry;
import utils.manifest.ManifestBuilder;

import config.user_config.storage_config.StorageConfigModel;
import config.user_config.storage_config.ConfigService;

import storage.LocalStorageReader;
import storage.SFTPStorageReader;

public class SelectiveRecoveryService implements Recoveryable {

    private final String targetFilename;
    private final String targetTimestamp; // the "at" field

    public SelectiveRecoveryService(String filename, String timestamp) {
        this.targetFilename = filename;
        this.targetTimestamp = timestamp;
    }

    @Override
    public void recover() throws Exception {
        Path recoveryWorkspace = Path.of("backup_workspace/temp/recovery_selective/");
        FileUtils.ensureDir(recoveryWorkspace);

        StorageConfigModel conf = ConfigService.getConfig();
        Path downloadDir = Path.of("backup_workspace/temp/recovery_download/");
        FileUtils.ensureDir(downloadDir);

        // Setup encryption handler
        String base64Key = conf.getEncryptionKey();
        EncryptionHandler encryptionHandler = EncryptionHandler.fromBase64Key(base64Key);
        EncryptionAdapter.setHandler(encryptionHandler);

        // Download files from storage
        switch (conf.getType()) {
            case LOCAL, EXTERNAL, PARTITION -> new LocalStorageReader().read(conf, downloadDir);
            case SFTP -> new SFTPStorageReader().read(conf, downloadDir);
            default -> throw new UnsupportedOperationException("Unsupported storage type: " + conf.getType());
        }

        // Prepare files to recover by pattern matching
        List<Path> filesToRecover = new ArrayList<>();
        for (Path file : Files.list(downloadDir).toList()) {
            String filename = file.getFileName().toString();
            if (filename.startsWith(targetFilename + "_") &&
                filename.endsWith(".gz.enc") &&
                filename.contains(targetTimestamp)) {
                filesToRecover.add(file);
                System.out.println("Matched encrypted file: " + filename);
            }
        }

        if (filesToRecover.isEmpty()) {
            System.out.println("No matching backup found for: " + targetFilename + " at " + targetTimestamp);
            return;
        }

        // Directly decrypt each file (deterministic, single-threaded)
        List<Path> decryptedFiles = new ArrayList<>();
        for (Path encFile : filesToRecover) {
            Path decrypted = EncryptionAdapter.decrypt(encFile);
            decryptedFiles.add(decrypted);
            System.out.println("File decrypted: " + decrypted.getFileName());
        }
        try{
            // Decompress decrypted files
            Compressable decompressor = new GzipCompressor();
            for (Path decrypted : decryptedFiles) {
                decompressor.decompress(decrypted, recoveryWorkspace);
                System.out.println("Decompressed: " + decrypted.getFileName());
            }
        } finally {
            FileUtils.cleanup(downloadDir);
            //FileUtils.cleanup(recoveryWorkspace);
            System.out.println("Temporary directories cleaned up.");
        }




        System.out.println("Selective recovery completed at " + recoveryWorkspace);
    }
}

