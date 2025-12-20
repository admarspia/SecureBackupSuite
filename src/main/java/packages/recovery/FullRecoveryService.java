package recovery;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.io.IOException;

import utils.compdecomp.FileUtils;
import utils.compdecomp.Compressable;
import utils.compdecomp.GzipCompressor;
import utils.encdecrypt.EncryptionAdapter;
import utils.encdecrypt.EncryptionHandler;
import utils.encdecrypt.DecryptorService;
import utils.Queues;

import config.user_config.storage_config.StorageConfigModel;
import config.user_config.storage_config.ConfigService;

import storage.LocalStorageReader;
import storage.SFTPStorageReader;
import utils.Logger;

public class FullRecoveryService implements Recoveryable {
    int workers = 3;

    @Override
    public void recover() throws Exception {

        Path recoveryWorkspace = Path.of("backup_workspace/temp/recovery/");
        Path downloadDir = Path.of("backup_workspace/temp/recovery_download/");

        FileUtils.ensureDir(recoveryWorkspace);
        FileUtils.ensureDir(downloadDir);

        StorageConfigModel conf = ConfigService.getConfig();

        EncryptionHandler handler =
            EncryptionHandler.fromBase64Key(conf.getEncryptionKey());
        EncryptionAdapter.setHandler(handler);

        switch (conf.getType()) {
            case LOCAL:
            case EXTERNAL:
            case PARTITION:
                new LocalStorageReader().read(conf, downloadDir);
                break;
            case SFTP:
                new SFTPStorageReader().read(conf, downloadDir);
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported storage type: " + conf.getType());
        }

        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(downloadDir, "*.enc")) {

            for (Path enc : stream) {
                Queues.ENCRYPTED_QUEUE.put(enc);
                System.out.println("Queued encrypted file: " + enc.getFileName());
            }
                }

        Queues.ENCRYPTED_QUEUE.put(Queues.POISON);

        DecryptorService decryptor = new DecryptorService(workers);
        decryptor.start();

        Compressable decompressor = new GzipCompressor();

        while (workers > 0) {
            Path decrypted = Queues.DECRYPTED_QUEUE.take();

            if (decrypted.equals(Queues.POISON)) {
                workers--;
                continue;
            }

            decompressor.decompress(decrypted, recoveryWorkspace);
            System.out.println("decompressed: " + decrypted.getFileName());

        }

        decryptor.stop();

        System.out.println("Full recovery completed at " + recoveryWorkspace);

        FileUtils.cleanup(Path.of("backup_workspace/temp/decrypted"));
        FileUtils.cleanup(Path.of("backup_workspace/temp/compressed"));
        FileUtils.cleanup(Path.of("backup_workspace/temp/recovery_download"));
        Logger.log("SUCCESS", "recovery", "recovery completed successfuly.");
    }

}

