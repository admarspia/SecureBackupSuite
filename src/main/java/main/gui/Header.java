package main.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.CheckBox;
import utils.manifest.*;

import backup.Backup;
import backup.BackupModel;
import recovery.Recovery;
import recovery.RecoveryModel;
import recovery.SelectiveRecoveryService;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.util.HashMap;
import java.util.Map;

public class Header {

    public static HBox getHeader() {

        Button btnLogs = new Button("Logs");
        Button btnStatus = new Button("Status");
        Button btnHelp = new Button("Help");
        Button btnConfig = new Button("Configure");

        ComboBox<String> backupCombo = new ComboBox<>();
        ComboBox<String> recoveryCombo = new ComboBox<>();

        backupCombo.getItems().addAll("Full-Backup", "Incremental", "Predictive");
        recoveryCombo.getItems().addAll("Full-Recovery", "Selective-Recovery");

        Stage backupPopup = new Stage();
        backupPopup.setTitle("Confirm Backup");
        Button backupOk = new Button("Backup");
        Button backupCancel = new Button("Cancel");
        VBox backupRoot = new VBox(10, new Label("Do you want to make a backup?"),
                new HBox(20, backupOk, backupCancel));
        backupPopup.setScene(new Scene(backupRoot));
        backupCancel.setOnAction(e -> backupPopup.close());

        Stage recoveryPopup = new Stage();
        recoveryPopup.setTitle("Confirm Recovery");
        Button recoveryOk = new Button("Recover");
        Button recoveryCancel = new Button("Cancel");
        VBox recoveryRoot = new VBox(10, new Label("Do you want to make a recovery?"),
                new HBox(20, recoveryOk, recoveryCancel));
        recoveryPopup.setScene(new Scene(recoveryRoot));
        recoveryCancel.setOnAction(e -> recoveryPopup.close());

        Stage selectiveStage = new Stage();
        TableView<ManifestEntry> filesTable = Status.getStatusTable();

        Button selectiveRecoverOk = new Button("Recover Selected");

        Button selectiveRecoverCancel = new Button("Cancel");
        selectiveRecoverCancel.setOnAction(e -> selectiveStage.close());
        VBox selectiveLayout = new VBox(10, new Label("Select Files to Recover"),
                filesTable, new HBox(20, selectiveRecoverOk, selectiveRecoverCancel));
        Scene selectiveScene = new Scene(selectiveLayout, 800, 600);
        selectiveStage.setTitle("Selective Recovery");
        selectiveStage.setScene(selectiveScene);

        selectiveRecoverOk.setOnAction(e -> {
            var selectedFiles = filesTable.getSelectionModel().getSelectedItems();
            if (!selectedFiles.isEmpty()) {
                Map<String, String> targetMap = new HashMap<>();
                for (ManifestEntry entry : selectedFiles) {
                    String[] fullPath = entry.original().toString().split("\\/");
                    String filename = fullPath[fullPath.length - 1];
                    targetMap.put(filename, entry.at());
                    System.out.println(entry.original().toString() + " " + entry.at());
                }
                SelectiveRecoveryService.setTarget(targetMap);
                try {
                    Recovery.recover(RecoveryModel.Type.SELECTIVE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                selectiveStage.close();
            }
        });

        backupCombo.setOnAction(e -> {
            String selected = backupCombo.getValue();
            backupPopup.show();
            backupOk.setOnAction(ok -> {
                switch (selected) {
                    case "Full-Backup" -> Backup.backup(BackupModel.Type.FULL);
                    case "Incremental" -> Backup.backup(BackupModel.Type.INCREMENTAL);
                    case "Predictive" -> System.out.println("Predictive backup coming soon.");
                }
                backupPopup.close();
            });
        });

        recoveryCombo.setOnAction(e -> {
            String selected = recoveryCombo.getValue();
            recoveryPopup.show();
            recoveryOk.setOnAction(ok -> {
                switch (selected) {
                    case "Full-Recovery" -> Recovery.recover(RecoveryModel.Type.FULL);
                    case "Selective-Recovery" -> selectiveStage.show();
                }
                recoveryPopup.close();
            });
        });

        btnLogs.setOnAction(e -> {
            Stage logStage = LogStage.getLogStage();
            if (logStage != null) logStage.show();
        });

        btnStatus.setOnAction(e -> {
            Stage statusStage = Status.getStatusStage();
            if (statusStage != null) statusStage.show();
        });

        btnHelp.setOnAction(e -> HelpStage.showHelp());


        btnConfig.setOnAction(e -> {
            try {
                File configFile = new File("config.yaml");
                if (!configFile.exists()) {
                    System.out.println("Config file not found.");
                    return;
                }

                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    new ProcessBuilder("notepad.exe", configFile.getAbsolutePath()).start();
                } else if (os.contains("mac")) {
                    new ProcessBuilder("open", configFile.getAbsolutePath()).start();
                } else { 
                    new ProcessBuilder("xdg-open", configFile.getAbsolutePath()).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        

        HBox header = new HBox(10, btnStatus, backupCombo, recoveryCombo, btnLogs, btnHelp, btnConfig);
        header.getStyleClass().add("header");

        return header;
    }
}

