package main.report;

import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class LogAnalyzer {
    
    private static final Path LOG_FILE = Path.of("backup_system.log");
    private static final DateTimeFormatter LOG_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSSS");
    
    public static LogReport generateReport() {
        LogReport report = new LogReport();
        
        try {
            List<String> lines = Files.readAllLines(LOG_FILE);
            
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue;
                }
                
                LogEntry entry = new LogEntry();
                entry.status = parts[0].trim();
                entry.source = parts[1].trim();
                entry.message = parts[2].trim();
                entry.timestamp = parts[3].trim();
                
                report.addEntry(entry);
            }
            
            
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        
        return report;
    }
    
    public static class LogEntry {
        String status;
        String source;
        String message;
        String timestamp;
       
        public String getStatus() { return status; }
        public String getSource() { return source; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }

        public boolean isSuccess() {
            return status.equalsIgnoreCase("SUCCESS");
        }
        
        public boolean isError() {
            return status.equalsIgnoreCase("ERROR");
        }
        
        public boolean isWarning() {
            return status.equalsIgnoreCase("WARNING");
        }
        
        public LocalDateTime getDateTime() {
            try {
                if (message.contains("at ")) {
                    String dateStr = message.substring(message.indexOf("at ") + 3);
                    return LocalDateTime.parse(dateStr.replace("+03:00[Africa/Addis_Ababa]", ""));
                }
                return LocalDateTime.now(); 
            } catch (Exception e) {
                return LocalDateTime.now();
            }
        }
    }
    
    public static class LogReport {
        private List<LogEntry> entries = new ArrayList<>();
        private Map<String, Integer> statusCount = new HashMap<>();
        private Map<String, Integer> sourceCount = new HashMap<>();
        private Map<String, Integer> errorSources = new HashMap<>();
        private List<LogEntry> errors = new ArrayList<>();
        private List<LogEntry> successes = new ArrayList<>();
        private Map<String, Integer> messageFrequency = new HashMap<>();
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private int totalEntries = 0;
        private int successCount = 0;
        private int errorCount = 0;
        
        public void addEntry(LogEntry entry) {
            entries.add(entry);
            totalEntries++;
            
            statusCount.merge(entry.status.toUpperCase(), 1, Integer::sum);
            
            sourceCount.merge(entry.source.toLowerCase(), 1, Integer::sum);
            
            if (entry.isError()) {
                errorCount++;
                errors.add(entry);
                errorSources.merge(entry.source, 1, Integer::sum);
            } else if (entry.isSuccess()) {
                successCount++;
                successes.add(entry);
            }
            
            String keyMessage = entry.message.length() > 50 ? 
                entry.message.substring(0, 50) + "..." : entry.message;
            messageFrequency.merge(keyMessage, 1, Integer::sum);
            
            LocalDateTime entryTime = entry.getDateTime();
            if (startTime == null || entryTime.isBefore(startTime)) {
                startTime = entryTime;
            }
            if (endTime == null || entryTime.isAfter(endTime)) {
                endTime = entryTime;
            }
        }
    
        public List<LogEntry> getEntries() { return entries; }
        public Map<String, Integer> getStatusCount() { return statusCount; }
        public Map<String, Integer> getSourceCount() { return sourceCount; }
        public Map<String, Integer> getErrorSources() { return errorSources; }
        public List<LogEntry> getErrors() { return errors; }
        public List<LogEntry> getSuccesses() { return successes; }
        public Map<String, Integer> getMessageFrequency() { return messageFrequency; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public int getTotalEntries() { return totalEntries; }
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        
        public double getSuccessRate() {
            return totalEntries > 0 ? (successCount * 100.0) / totalEntries : 0;
        }
        
        public String getMostActiveSource() {
            return sourceCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        }
        
        public String getMostCommonError() {
            return errorSources.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        }
    }
}
