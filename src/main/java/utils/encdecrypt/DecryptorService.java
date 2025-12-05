package utils.encdecrypt;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import utils.Logger;
import utils.Queues;


  
public class DecryptorService {
    private final int workerCount;
    private final ExecutorService workers;

    public DecryptorService(int workerCount) {
        this.workerCount = Math.max(1, workerCount);
        this.workers = Executors.newFixedThreadPool(this.workerCount);
    }

    public void start() {
        for (int i = 0; i < workerCount; i++) {
            workers.submit(() -> {
                try {
                    while (true) {
                        Path encrypted = Queues.ENCRYPTED_QUEUE.take();
                        if (encrypted.equals(Queues.POISON)) {
                            Queues.ENCRYPTED_QUEUE.put(Queues.POISON);
                            Queues.DECRYPTED_QUEUE.put(Queues.POISON);
                            break;
                        }

                        try {
                            Path decrypted = EncryptionAdapter.decrypt(encrypted);
                            Queues.DECRYPTED_QUEUE.put(decrypted);
                        } catch (Exception ex) {
                            Logger.log("Decryption failed for " + encrypted + ": " + ex);
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void stop() throws InterruptedException {
        workers.shutdown();
        if (!workers.awaitTermination(1, TimeUnit.MINUTES)) {
            workers.shutdownNow();
        }
    }
}

