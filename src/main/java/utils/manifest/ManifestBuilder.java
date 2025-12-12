package utils.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.nio.file.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;

import backup.BackupModel;

public class ManifestBuilder {

    private final List<ManifestEntry> entries = new ArrayList<>();
    private final Path manifestFile;
    private final BackupModel.Type backupType;
    private final ZonedDateTime timestamp;

    public ManifestBuilder(Path manifestDir, BackupModel.Type backupType) throws IOException {
        this.backupType = backupType;
        this.timestamp = ZonedDateTime.now();

        Files.createDirectories(manifestDir);
        this.manifestFile = manifestDir.resolve("manifest.json");

        loadExistingManifest();
    }

    private void loadExistingManifest() {
        if (!Files.exists(manifestFile)) return;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(manifestFile.toFile());
            JsonNode arr = root.get("entries");

            if (arr != null && arr.isArray()) {
                for (JsonNode node : arr) {
                    String originalText = node.get("original").asText();
                    URI uri = URI.create(originalText);
                    Path originalPath = Paths.get(uri);

                    ManifestEntry entry = new ManifestEntry(
                            originalPath,
                            node.get("hash").asText(),
                            node.get("originalSize").asLong(),
                            node.get("at").asText()
                    );
                    entries.add(entry);
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to load existing manifest: " + ex.getMessage());
        }
    }

    public synchronized void addEntry(ManifestEntry entry) {
        entries.removeIf(e -> e.hash().equals(entry.hash()));
        entries.add(entry);
    }

    public synchronized void save() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        var manifestObject = new ManifestRoot(
                backupType,
                timestamp.toString(),
                entries
        );

        mapper.writeValue(manifestFile.toFile(), manifestObject);
    }

    public synchronized List<ManifestEntry> getLoadedEntries() {
        return new ArrayList<>(entries); 
    }

    public record ManifestRoot(
            BackupModel.Type backupType,
            String timestamp,
            List<ManifestEntry> entries
    ) {}
}

