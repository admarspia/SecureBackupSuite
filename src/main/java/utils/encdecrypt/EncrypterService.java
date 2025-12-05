package utils.compdecomp;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import utils.Logger;

public class EncrypterService {

    private final int workerCount;
    private final ExecutorService workers;

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
                        if (compressed.equals(Queues.POISON)) {
                            Queues.COMPRESSED_QUEUE.put(Queues.POISON); 
                            Queues.ENCRYPTED_QUEUE.put(Queues.POISON);
                            break;
                        }

                        try {
                            Path encrypted = EncryptionAdapter.encrypt(compressed); 
                            Queues.ENCRYPTED_QUEUE.put(encrypted);
                        } catch (Exception ex) {
                            Logger.log("Encryption failed for " + compressed + ": " + ex);
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
        workers.awaitTermination(1, TimeUnit.MINUTES);
    }
}

