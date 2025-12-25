package main.gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class LogView {
    
    public static VBox getContent() {
        ObservableList<Log> logs = parseLogs();
        TableView<Log> table = createLogTable(logs);
        
        VBox content = new VBox(10);
        content.getStyleClass().add("card");
        content.setPadding(new javafx.geometry.Insets(20));
        
        Label title = new Label("System Logs");
        title.getStyleClass().add("title");
        
        Label subtitle = new Label("Complete log for backup and recovery operations");
        subtitle.getStyleClass().add("subtitle");
        
        content.getChildren().addAll(title, subtitle, table);
        return content;
    }
    
    private static TableView<Log> createLogTable(ObservableList<Log> logs) {
        TableView<Log> table = new TableView<>(logs);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Log, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(100);
        
        TableColumn<Log, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSource()));
        sourceCol.setPrefWidth(150);
        
        TableColumn<Log, String> msgCol = new TableColumn<>("Message");
        msgCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMsg()));
        msgCol.setPrefWidth(400);
        
        TableColumn<Log, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        dateCol.setPrefWidth(150);
        
        table.getColumns().addAll(statusCol, sourceCol, msgCol, dateCol);
        table.setPrefWidth(1100);
        table.setPrefHeight(500);
        
        return table;
    }
    
    private static ObservableList<Log> parseLogs() {
        ObservableList<Log> logs = FXCollections.observableArrayList();
        Path logFile = Path.of("backup_system.log");
        
        if (!Files.exists(logFile)) {
            Log noLogs = new Log();
            noLogs.setStatus("INFO");
            noLogs.setSource("System");
            noLogs.setMsg("No log file found");
            noLogs.setDate("-");
            logs.add(noLogs);
            return logs;
        }
        
        try {
            List<String> lines = Files.readAllLines(logFile);
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] tokens = line.split("\\|");
                if (tokens.length < 4) continue;
                
                Log log = new Log();
                log.setStatus(tokens[0].trim());
                log.setSource(tokens[1].trim());
                log.setMsg(tokens[2].trim());
                log.setDate(tokens[3].trim());
                logs.add(log);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return logs;
    }
}

class Log {
    private String status;
    private String source;
    private String msg;
    private String date;

    public String getStatus() { return status; }
    public String getSource() { return source; }
    public String getMsg() { return msg; }
    public String getDate() { return date; }

    public void setStatus(String status) { this.status = status; }
    public void setSource(String source) { this.source = source; }
    public void setMsg(String msg) { this.msg = msg; }
    public void setDate(String date) { this.date = date; }
}
