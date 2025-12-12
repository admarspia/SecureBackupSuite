package utils.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ManifestDisplay {

    public static void display()  {
        try {
        Path manifestFile = Path.of("backup_workspace/temp/archive/manifest.json");
        if (!Files.exists(manifestFile)) {
            System.out.println("Manifest file not found!");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(manifestFile.toFile());
        JsonNode entries = root.get("entries");

        if (entries == null || !entries.isArray()) {
            System.out.println("No entries in manifest.");
            return;
        }

        System.out.printf("%-50s %-12s %-30s%n", "FILENAME", "SIZE", "TIMESTAMP");
        System.out.println("-------------------------------------------------------------------------------------");

        for (JsonNode entry : entries) {
            String original = entry.get("original").asText();
            long size = entry.get("originalSize").asLong();
            String time = entry.has("at") ? entry.get("at").asText() : "";

            System.out.printf(
                "%-50s %-12s %-30s%n",
                Path.of(original).getFileName().toString(),
                humanReadableSize(size),
                time
            );
        }
    } catch (IOException ex){
        System.out.println("Error: " + ex);
    }

    }
    private static String humanReadableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}

