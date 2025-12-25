package main.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import utils.manifest.*;

import backup.Backup;
import backup.BackupModel;
import recovery.Recovery;
import recovery.RecoveryModel;
import recovery.SelectiveRecoveryService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Header {

    public static HBox getHeader() {
        Button btnWelcome = new Button("Welcome");
        Button btnStatus = new Button("Status");
        Button btnLogs = new Button("Logs");
        Button btnHelp = new Button("Help");
        Button btnConfig = new Button("Configure");

        ComboBox<String> backupCombo = new ComboBox<>();
        ComboBox<String> recoveryCombo = new ComboBox<>();

        // buttons for navigation 
        btnWelcome.setOnAction(e -> NavigationManager.navigateToWelcome());
        btnStatus.setOnAction(e -> NavigationManager.navigateToStatus());
        btnLogs.setOnAction(e -> NavigationManager.navigateToLogs());
        btnHelp.setOnAction(e -> NavigationManager.navigateToHelp());

        backupCombo.getItems().addAll("Full-Backup", "Incremental");
        recoveryCombo.getItems().addAll("Full-Recovery", "Selective-Recovery");

        backupCombo.setPromptText("Select Backup Type");
        recoveryCombo.setPromptText("Select Recovery Type");
       
        backupCombo.getStyleClass().add("prompt-styled");
        recoveryCombo.getStyleClass().add("prompt-styled");

        Stage backupPopup = new Stage();
        backupPopup.initModality(Modality.APPLICATION_MODAL); //blocking every window until this is closed.

        backupPopup.initOwner(NavigationManager.getPrimaryStage());
        backupPopup.setTitle("Confirm Backup");
        Button backupOk = new Button("Backup");
        Button backupCancel = new Button("Cancel");
        VBox backupRoot = new VBox(10, new Label("Do you want to make a backup?"),
                new HBox(20, backupOk, backupCancel));
        backupRoot.setPadding(new javafx.geometry.Insets(20));
        backupPopup.setScene(new Scene(backupRoot, 300, 150));
        backupCancel.setOnAction(e -> backupPopup.close());

        // creating recormry and config popup
        Stage recoveryPopup = new Stage();
        recoveryPopup.initModality(Modality.APPLICATION_MODAL);
        recoveryPopup.initOwner(NavigationManager.getPrimaryStage());
        recoveryPopup.setTitle("Confirm Recovery");
        Button recoveryOk = new Button("Recover");
        Button recoveryCancel = new Button("Cancel");
        VBox recoveryRoot = new VBox(10, new Label("Do you want to make a recovery?"),
                new HBox(20, recoveryOk, recoveryCancel));
        recoveryRoot.setPadding(new javafx.geometry.Insets(20));
        recoveryPopup.setScene(new Scene(recoveryRoot, 300, 150));
        recoveryCancel.setOnAction(e -> recoveryPopup.close());

        Stage selectiveStage = null;
        TableView<ManifestEntry> selectiveTable = null;

        backupCombo.setOnAction(e -> {
            String selected = backupCombo.getValue();
            if (selected != null) {
                backupPopup.show();
                backupOk.setOnAction(ok -> {
                    switch (selected) {
                        case "Full-Backup" -> Backup.backup(BackupModel.Type.FULL);
                        case "Incremental" -> Backup.backup(BackupModel.Type.INCREMENTAL);
                    }
                    backupPopup.close();
                    backupCombo.getSelectionModel().clearSelection();
                });
            }
        });

        // recovery combo
        recoveryCombo.setOnAction(e -> {
            String selected = recoveryCombo.getValue();
            if (selected != null) {
                if ("Selective-Recovery".equals(selected)) {
                    showSelectiveRecoveryDialog();
                } else {
                    recoveryPopup.show();
                    recoveryOk.setOnAction(ok -> {
                        if ("Full-Recovery".equals(selected)) {
                            Recovery.recover(RecoveryModel.Type.FULL);
                        }
                        recoveryPopup.close();
                        recoveryCombo.getSelectionModel().clearSelection();
                    });
                }
            }
        });

        // config btn handler 
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

        // report button
        Button btnReport = new Button("Report");
        btnReport.setOnAction(e -> NavigationManager.navigateToReport());


        HBox header = new HBox(10, 
                btnWelcome, btnStatus, btnReport, backupCombo, recoveryCombo, 
                btnLogs, btnHelp, btnConfig
                );

        header.getStyleClass().add("header");
        header.setPadding(new javafx.geometry.Insets(15));

        return header;
    }
    
    private static void showSelectiveRecoveryDialog() {
        TableView<ManifestEntry> filesTable = StatusView.createSelectiveRecoveryTable();
        
        Stage selectiveStage = new Stage();
        selectiveStage.initModality(Modality.APPLICATION_MODAL);
        selectiveStage.initOwner(NavigationManager.getPrimaryStage());
        selectiveStage.setTitle("Selective Recovery");
        
        Button selectiveRecoverOk = new Button("Recover Selected");
        Button selectiveRecoverCancel = new Button("Cancel");
        
        selectiveRecoverCancel.setOnAction(e -> selectiveStage.close());
        
        VBox selectiveLayout = new VBox(10, 
            new Label("Select Files to Recover"),
            filesTable, 
            new HBox(20, selectiveRecoverOk, selectiveRecoverCancel)
        );
        selectiveLayout.setPadding(new javafx.geometry.Insets(20));
        
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
        
        Scene selectiveScene = new Scene(selectiveLayout, 900, 600);
        selectiveStage.setScene(selectiveScene);
        selectiveStage.show();
    }
}
