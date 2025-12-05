package utils.compdecomp;

import java.nio.file.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import utils.Logger;
import utils.Queues;
import java.util.Objects;

public class CompressionManager {

    private final Compressable compressor;
    private final Path tempDir;
    private final ExecutorService workers;

    public CompressionManager(Compressable compressor, Path tempDir, int threads) {
        this.compressor = Objects.requireNonNull(compressor);
        this.tempDir = Objects.requireNonNull(tempDir);
        this.workers = Executors.newFixedThreadPool(Math.max(1, threads));
    }

    public void compressAll(Set<Path> sources, Set<String> includes, boolean recursive) throws IOException, InterruptedException {
        for (Path srcDir : sources) {
            if (srcDir == null) continue;
            for (String pattern : includes) {
                List<Path> files = FileUtils.findFiles(srcDir, pattern, recursive);
                for (Path file : files) {
                    final Path f = file;
                    workers.submit(() -> {
                        try {
                            Path compressed = compressor.compress(f, tempDir);
                            Queues.COMPRESSED_QUEUE.put(compressed);
                        } catch (Exception ex) {
                            Logger.log("Compress failed for " + f + ": " + ex);
                        }
                    });
                }
            }
        }

        workers.shutdown();
        if (!workers.awaitTermination(1, TimeUnit.HOURS)) {
            workers.shutdownNow();
        }

        Queues.COMPRESSED_QUEUE.put(Queues.POISON);
    }
}

