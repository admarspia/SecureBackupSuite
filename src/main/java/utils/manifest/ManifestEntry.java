package utils.manifest;

import java.nio.file.Path;

public record ManifestEntry(
        Path original,
        String hash,
        long originalSize,
        String at

) {}

