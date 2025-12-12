package utils.compdecomp;

import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {
    private FileUtils() {}

    public static List<Path> findFiles(Path dir, String globPattern, boolean recursive) throws IOException {
        if (dir == null || !Files.isDirectory(dir)) return List.of();

        String pattern = "glob:" + globPattern;
        PathMatcher matcher = dir.getFileSystem().getPathMatcher(pattern);

        try (Stream<Path> s = recursive ? Files.walk(dir) : Files.list(dir)) {
            return s.filter(Files::isRegularFile)
                    .filter(p -> matcher.matches(p.getFileName()))
                    .distinct() 
                    .filter(p -> !p.getFileName().toString().startsWith(".~lock"))
                    .collect(Collectors.toList());
        }
    }

    public static Path ensureDir(Path dir) throws IOException {
        if (dir == null) throw new IllegalArgumentException("dir must not be null");
        if (!Files.exists(dir)) Files.createDirectories(dir);
        return dir;
    }

    public static Path tempFile(Path dir, String prefix, String suffix) throws IOException {
        ensureDir(dir);
        return Files.createTempFile(dir, prefix, suffix);
    }

    public static void deleteDirectoryRecursively(Path dir) throws IOException {
        if (Files.notExists(dir)) return;

        Files.walk(dir)
             .sorted((a, b) -> b.compareTo(a)) 
             .forEach(path -> {
                 try {
                     Files.delete(path);
                 } catch (IOException e) {
                     throw new RuntimeException("Failed to delete: " + path, e);
                 }
             });
    }

    public  static void cleanup(Path dir) {
        if (Files.exists(dir)) {
            try {
                FileUtils.deleteDirectoryRecursively(dir);
            } catch (IOException e) {
                System.err.println("Failed to clean temp directory " + dir + ": " + e.getMessage());
            }
        }
    }
}

