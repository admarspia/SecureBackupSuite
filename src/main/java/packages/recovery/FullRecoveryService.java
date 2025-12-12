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

public class FullRecoveryService implements Recoveryable {

    @Override
    public void recover() throws Exception {
        Path recoveryWorkspace = Path.of("backup_workspace/temp/recovery/");
        FileUtils.ensureDir(recoveryWorkspace);

        StorageConfigModel conf = ConfigService.getConfig();
        Path downloadDir = Path.of("backup_workspace/temp/recovery_download/");
        FileUtils.ensureDir(downloadDir);

        String base64Key = conf.getEncryptionKey();
        EncryptionHandler encryptionHandler = EncryptionHandler.fromBase64Key(base64Key);
        EncryptionAdapter.setHandler(encryptionHandler);

        switch (conf.getType()) {
            case LOCAL:
            case EXTERNAL:
            case PARTITION:
                LocalStorageReader localReader = new LocalStorageReader();
                localReader.read(conf, downloadDir);
                break;

            case SFTP:
                SFTPStorageReader sftpReader = new SFTPStorageReader();
                sftpReader.read(conf, downloadDir);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported storage type: " + conf.getType());
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(downloadDir, "*.enc")) {
            for (Path encFile : stream) {
                Queues.ENCRYPTED_QUEUE.put(encFile);
                System.out.println("Queued encrypted file: " + encFile.getFileName());
            }
        }

        Queues.ENCRYPTED_QUEUE.put(Queues.POISON);

        DecryptorService decryptor = new DecryptorService(3);
        decryptor.start();

        while (true) {
            Path encrypted = Queues.ENCRYPTED_QUEUE.take();
            if (encrypted.equals(Queues.POISON)) {
                Queues.ENCRYPTED_QUEUE.put(Queues.POISON);
                break;
            }
            Path decrypted = EncryptionAdapter.decrypt(encrypted);
            Queues.DECRYPTED_QUEUE.put(decrypted);
        }

        decryptor.stop();

        Compressable decompressor = new GzipCompressor();
        while (!Queues.DECRYPTED_QUEUE.isEmpty()) {
            Path decrypted = Queues.DECRYPTED_QUEUE.take();
            if (decrypted.equals(Queues.POISON)) {
                System.out.println("reached poison.die");
            };
            decompressor.decompress(decrypted, recoveryWorkspace);
        }

        System.out.println("Full recovery completed at " + recoveryWorkspace);


        FileUtils.cleanup(Path.of("backup_workspace/temp/compressed"));
        FileUtils.cleanup(Path.of("backup_workspace/temp/decrypted"));

    }
}

