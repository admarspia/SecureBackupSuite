package main.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

public class LogStage {

    public static Stage getLogStage() {

        Stage stage = new Stage();
        stage.setTitle("Backup-And-Recovery-Logs");

        BorderPane root = new BorderPane();

        HBox header = Header.getHeader();
        root.setTop(header);

        ObservableList<Log> logs = ParseLog.parse();

        TableView<Log> table = new TableView<>();
        table.setItems(logs);

        TableColumn<Log, String> statusClmn = new TableColumn<>("Status");
        statusClmn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getStatus())
        );

        TableColumn<Log, String> sourceClmn = new TableColumn<>("Source");
        sourceClmn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getSource())
        );

        TableColumn<Log, String> msgClmn = new TableColumn<>("Message");
        msgClmn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getMsg())
        );

        TableColumn<Log, String> dateClmn = new TableColumn<>("Date");
        dateClmn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getDate())
        );

        table.getColumns().addAll(statusClmn, sourceClmn, msgClmn, dateClmn);

        VBox content = new VBox(
                10,
                new Label("Complete log for backup and recovery system"),
                table
        );

        table.setPrefWidth(1000);
        table.setPrefHeight(600);

        content.getStyleClass().add("card");

        //content.getStyleClass().add("card");
        
        root.setCenter(content);

        Scene scene = new Scene(root, 900, 500);

        scene.getStylesheets().add(
            LogStage.class.getResource("/styles/app.css").toExternalForm()
        );
        stage.setScene(scene);

        AppIcon.apply(stage);


        return stage;
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


class ParseLog {

    private static final Path logFile = Path.of("backup_system.log");

    public static ObservableList<Log> parse() {

        ObservableList<Log> logs = FXCollections.observableArrayList();

        if (!Files.exists(logFile)) {
            System.out.println("Log file not found");
            return logs;
        }

        try {
            List<String> lines = Files.readAllLines(logFile);

            for (String line : lines) {

                if (line == null || line.isBlank()) continue;

                String[] tokens = line.split("\\|");

                if (tokens.length < 4) continue;

                Log log = new Log();
                log.setStatus(tokens[0]);
                log.setSource(tokens[1]);
                log.setMsg(tokens[2]);
                log.setDate(tokens[3]);

                logs.add(log);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return logs;
    }
}

