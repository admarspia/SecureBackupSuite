package utils.compdecomp;

import java.nio.file.*;
import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class GzipCompressor implements Compressable {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public Path compress(Path source, Path targetDir) throws IOException {
        Objects.requireNonNull(source);
        Objects.requireNonNull(targetDir);

        if (!Files.isRegularFile(source)) {
            throw new IllegalArgumentException("Source must be a regular file: " + source);
        }

        FileUtils.ensureDir(targetDir);

        String base = source.getFileName().toString();
        String timestamp = ZonedDateTime.now(ZoneId.systemDefault()).format(TS);
        String outName = base + "_" + timestamp + ".gz";

        Path temp = FileUtils.tempFile(targetDir, base + "_" + timestamp, ".tmp");
        Path target = targetDir.resolve(outName);

        try (InputStream in = Files.newInputStream(source);
             OutputStream fout = Files.newOutputStream(temp);
             BufferedOutputStream bout = new BufferedOutputStream(fout);
             GZIPOutputStream gzip = new GZIPOutputStream(bout)) {

            in.transferTo(gzip);
            gzip.finish();
        }

        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    @Override
    public Path decompress(Path compressedFile, Path targetDir) throws IOException {
        Objects.requireNonNull(compressedFile);
        Objects.requireNonNull(targetDir);

        if (!Files.isRegularFile(compressedFile)) {
            throw new IllegalArgumentException("Source must be a regular file: " + compressedFile);
        }

        FileUtils.ensureDir(targetDir);

        String name = compressedFile.getFileName().toString();
        
        int start = name.length() - 17;
        String ts = name.substring(start, name.length() - 3);

        String filename = name.endsWith(".gz") ? name.substring(0, name.length() - 18) : name;
        String outName = ts + "_" + filename;

        Path temp = FileUtils.tempFile(targetDir, outName, ".tmp");
        Path target = targetDir.resolve(outName);

        try (InputStream fin = Files.newInputStream(compressedFile);
             BufferedInputStream bin = new BufferedInputStream(fin);
             GZIPInputStream gis = new GZIPInputStream(bin);
             OutputStream out = Files.newOutputStream(temp)) {

            gis.transferTo(out);
        }

        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}

