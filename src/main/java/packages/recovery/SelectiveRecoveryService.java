package recovery;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import utils.compdecomp.FileUtils;
import utils.compdecomp.Compressable;
import utils.compdecomp.GzipCompressor;
import utils.encdecrypt.EncryptionAdapter;
import utils.encdecrypt.EncryptionHandler;

import config.user_config.storage_config.StorageConfigModel;
import config.user_config.storage_config.ConfigService;

import storage.LocalStorageReader;
import storage.SFTPStorageReader;

import user.UserUI;

public class SelectiveRecoveryService implements Recoveryable {

    private static Map<String, String> target = new HashMap<>();


    public static void setTarget(Map<String, String> t) {
        target.clear();
        target.putAll(t);
    }


    @Override
    public void recover() throws Exception {

        UserUI.receiveTargetFilename();

        Path recoveryWorkspace = Path.of("backup_workspace/temp/recovery_selective/");
        FileUtils.ensureDir(recoveryWorkspace);

        StorageConfigModel conf = ConfigService.getConfig();
        Path downloadDir = Path.of("backup_workspace/temp/recovery_download/");

        FileUtils.ensureDir(downloadDir);

        String base64Key = conf.getEncryptionKey();
        EncryptionHandler encryptionHandler = EncryptionHandler.fromBase64Key(base64Key);
        EncryptionAdapter.setHandler(encryptionHandler);

        switch (conf.getType()) {
            case LOCAL, EXTERNAL, PARTITION -> new LocalStorageReader().read(conf, downloadDir);
            case SFTP -> new SFTPStorageReader().read(conf, downloadDir);
            default -> throw new UnsupportedOperationException("Unsupported storage type: " + conf.getType());
        }

        List<Path> filesToRecover = new ArrayList<>();

        for (Map.Entry<String, String> entry : target.entrySet()) {
            String targetFilename = entry.getKey();
            String targetTimestamp = entry.getValue();

            for (Path file : Files.list(downloadDir).toList()) {
                String filename = file.getFileName().toString();

                if (
                    filename.startsWith(targetFilename + "_") &&
                    filename.endsWith(".gz.enc") &&
                    filename.contains(targetTimestamp)
                ) {
                    filesToRecover.add(file);
                    System.out.println("Matched encrypted file: " + filename);
                }
            }
        }

        if (filesToRecover.isEmpty()) {
            System.out.println("No matching files found for selective recovery.");
            return;
        }

        List<Path> decryptedFiles = new ArrayList<>();

        try {
            for (Path encFile : filesToRecover) {
                Path decrypted = EncryptionAdapter.decrypt(encFile);
                decryptedFiles.add(decrypted);
                System.out.println("File decrypted: " + decrypted.getFileName());
            }

            Compressable decompressor = new GzipCompressor();
            for (Path decrypted : decryptedFiles) {
                decompressor.decompress(decrypted, recoveryWorkspace);
                System.out.println("Decompressed: " + decrypted.getFileName());
            }
        } finally {
            FileUtils.cleanup(downloadDir);

        }

        System.out.println("Temporary directories cleaned up.");

        System.out.println("Selective recovery completed at " + recoveryWorkspace);
    }
}

