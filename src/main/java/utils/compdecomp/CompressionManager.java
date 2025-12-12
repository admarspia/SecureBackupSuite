package utils.compdecomp;

import java.nio.file.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import backup.BackupModel;
import utils.Logger;
import utils.Queues;
import utils.manifest.*;
import java.util.Objects;
import java.time.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class CompressionManager {

    private final Compressable compressor;
    private final Path tempDir;
    private final ExecutorService workers;
    private ManifestBuilder manifest;
    private String timestamp = ZonedDateTime.now(ZoneId.systemDefault()).format(TS);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


    public CompressionManager(Compressable compressor, Path tempDir, int threads, BackupModel.Type type) throws IOException {
        this.compressor = Objects.requireNonNull(compressor);
        this.tempDir = Objects.requireNonNull(tempDir);
        this.workers = Executors.newFixedThreadPool(Math.max(1, threads));
        manifest = new ManifestBuilder(Path.of("backup_workspace/temp/archive"),type);

    }

    public void compressAll(Set<String> sources, Set<String> includes, boolean recursive) throws IOException, InterruptedException {
        for (String srcDir : sources) {

            if (srcDir == null) continue;
            for (String pattern : includes) {
                List<Path> files = FileUtils.findFiles(Path.of(srcDir), pattern, recursive);
                for (Path file : files) {
                    final Path f = file;

                    String name = f.getFileName().toString();
                    if (name.startsWith(".")) {
                        System.out.println("Skipping lock file: " + name);
                        continue;
                    }

                    workers.submit(() -> {
                        try {
                            Path compressed = compressor.compress(f, tempDir);
                            Queues.COMPRESSED_QUEUE.put(compressed);
                            manifest.addEntry(new ManifestEntry(
                                        f,
                                        FileHashing.sha256(f),
                                        Files.size(f),
                                        timestamp
                                        ));
                            System.out.println(f + " " + Files.size(f) +  FileHashing.sha256(f) + "\tcompressed");

                        } catch (Exception ex) {
                            Logger.log("ERROR", "compression",ex.getMessage());
                            System.out.println("Error: " + ex);
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
        manifest.save();

    }

    public void compressFiles(Set<Path> files) throws IOException, InterruptedException {
        for (Path file : files) {
            if (file == null) continue;

            workers.submit(() -> {
                try {
                    Path compressed = compressor.compress(file, tempDir);
                    Queues.COMPRESSED_QUEUE.put(compressed);

                    manifest.addEntry(new ManifestEntry(
                                file,
                                FileHashing.sha256(file),
                                Files.size(file),
                                timestamp
                                ));

                    System.out.println(file + " compressed");

                } catch (Exception ex) {
                    Logger.log("ERROR", "compression", ex.getMessage());
                }
            });
        }

        workers.shutdown();
        if (!workers.awaitTermination(1, TimeUnit.HOURS)) {
            workers.shutdownNow();
        }

        Queues.COMPRESSED_QUEUE.put(Queues.POISON);
        manifest.save();
    }


}

