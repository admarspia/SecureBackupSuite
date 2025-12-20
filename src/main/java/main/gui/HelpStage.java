package main.gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpStage {

    public static void showHelp() {
        Stage stage = new Stage();
        stage.setTitle("Backup System Help");

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("card");

        Label title = new Label("BACKUP SYSTEM");
        title.getStyleClass().add("title");

        Label description = new Label(
            "Secure, modular backup utility.\n" +
            "Designed for reliable data protection using compression, encryption, and multiple storage backends."
        );
        description.setWrapText(true);

        Label usageLabel = new Label("USAGE");
        usageLabel.getStyleClass().add("section-title");
        Label usage = new Label("backup <command> [options]");

        Label commandsLabel = new Label("COMMANDS");
        commandsLabel.getStyleClass().add("section-title");
        Label commands = new Label(
            "init: Initialize workspace and configuration files.\n" +
            "backup: Run backup operation.\n" +
            "restore: Restore files from backup.\n" +
            "status: Display backup status.\n" +
            "config: View or modify configuration.\n" +
            "help: Show this help page."
        );
        commands.setWrapText(true);

        Label pipelineLabel = new Label("BACKUP PIPELINE");
        pipelineLabel.getStyleClass().add("section-title");
        Label pipeline = new Label(
            "1. File discovery\n" +
            "2. Compression (gzip)\n" +
            "3. Encryption (AES-based)\n" +
            "4. Storage to target location\n" +
            "5. Manifest generation and verification"
        );

        Label storageLabel = new Label("SUPPORTED STORAGE TARGETS");
        storageLabel.getStyleClass().add("section-title");
        Label storage = new Label("• Local filesystem\n• External devices\n• SFTP servers");

        Label securityLabel = new Label("SECURITY FEATURES");
        securityLabel.getStyleClass().add("section-title");
        Label security = new Label(
            "• Encrypted backup archives\n" +
            "• SHA-256 integrity verification\n" +
            "• Password-based key derivation\n" +
            "• Manifest-based recovery"
        );

        Label workspaceLabel = new Label("WORKSPACE LAYOUT");
        workspaceLabel.getStyleClass().add("section-title");
        Label workspace = new Label(
            "./backup_workspace/\n" +
            "├── config.yaml\n" +
            "├── backups/\n" +
            "├── manifests/\n" +
            "└── temp/"
        );

        Label examplesLabel = new Label("EXAMPLES");
        examplesLabel.getStyleClass().add("section-title");
        Label examples = new Label(
            "Initialize workspace:\nbackup init\n\n" +
            "Run a backup:\nbackup backup\n\n" +
            "Restore files:\nbackup restore\n\n" +
            "Check status:\nbackup status"
        );

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("button");
        closeBtn.setOnAction(e -> stage.close());

        content.getChildren().addAll(
            title, description,
            usageLabel, usage,
            commandsLabel, commands,
            pipelineLabel, pipeline,
            storageLabel, storage,
            securityLabel, security,
            workspaceLabel, workspace,
            examplesLabel, examples,
            closeBtn
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, 700, 600);
        scene.getStylesheets().add(
            HelpStage.class.getResource("/styles/app.css").toExternalForm()
        );

        AppIcon.apply(stage);

        stage.setScene(scene);
        stage.show();
    }
}

