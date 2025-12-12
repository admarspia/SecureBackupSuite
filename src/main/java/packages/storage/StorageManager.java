package storage;

import java.nio.file.Path;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import utils.Queues;
import config.user_config.storage_config.StorageConfigModel;
import utils.Logger;

public class StorageManager {
    public static  int workerCount;
    private final ExecutorService workers;
    private final StorageWriter writer;
    private final StorageConfigModel config;
    private boolean running = false;

    public StorageManager(StorageWriter writer, StorageConfigModel config, int threads) {
        this.writer = writer;
        this.workerCount = threads;
        this.workers = Executors.newFixedThreadPool(workerCount);
        this.config =  config;
    }

    public void start() {
        for (int i = 0; i < workerCount; i++) {
            workers.submit(() -> {
                try {
                    while (true) {
                        Path encrypted = Queues.ENCRYPTED_QUEUE.take();
                        if (encrypted.equals(Queues.POISON)) break;
                        writer.write(config, encrypted);
                        System.out.println(encrypted.getFileName() + " written");
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    Logger.log("ERROR", "storage", e.getMessage());
                }
            });
        }
    }

    public void stop() throws InterruptedException {
        workers.shutdown();
        workers.awaitTermination(1, TimeUnit.HOURS);
    }

}

