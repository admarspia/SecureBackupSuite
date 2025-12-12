package utils.encdecrypt;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import utils.Logger;
import utils.Queues;
import storage.StorageManager;

public class EncrypterService {

    private final int workerCount;
    private final ExecutorService workers;
    private boolean running = false;

    public EncrypterService(int workerCount) {
        this.workerCount = workerCount;
        this.workers = Executors.newFixedThreadPool(workerCount);
    }

    public void start() {
        for (int i = 0; i < workerCount; i++) {
            workers.submit(() -> {
                try {
                    while (true) {
                        Path compressed = Queues.COMPRESSED_QUEUE.take();
                        if (compressed.equals(Queues.POISON)) break;

                        try {
                            Path encrypted = EncryptionAdapter.encrypt(compressed);
                            System.out.println(encrypted.getFileName() + " Encrypted");
                            Queues.ENCRYPTED_QUEUE.put(encrypted);
                        } catch (Exception ex) {
                            Logger.log("Error", "encryption", ex.getMessage());
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void stop() throws InterruptedException {
        // signal the workers to exit
        for (int i = 0; i < workerCount; i++) {
            Queues.COMPRESSED_QUEUE.put(Queues.POISON);
        }
        workers.shutdown();
        workers.awaitTermination(1, TimeUnit.HOURS);
        // signal storage workers that encryption is done
        for (int i = 0; i < StorageManager.workerCount; i++) {
            Queues.ENCRYPTED_QUEUE.put(Queues.POISON);
        }
    }

}

