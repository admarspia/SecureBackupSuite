package main.gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class HelpView {
    
    public static ScrollPane getContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");
        
        Label title = new Label("Backup System Help");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Documentation and usage guide");
        subtitle.setTextFill(Color.web("#7f8c8d"));
        subtitle.setFont(Font.font(14));
        
        VBox headerBox = new VBox(5, title, subtitle);
        
        VBox sections = new VBox(25);
        
        sections.getChildren().addAll(
            createSection("Usage", "backup <command> [options]"),
            createSection("Commands", 
                "• init: Initialize workspace and configuration\n" +
                "• backup: Run backup operation\n" +
                "• restore: Restore files from backup\n" +
                "• status: Display backup status\n" +
                "• config: View or modify configuration\n" +
                "• help: Show this help page"),
            createSection("Backup Pipeline",
                "1. File discovery\n" +
                "2. Compression (gzip)\n" +
                "3. Encryption (AES-based)\n" +
                "4. Storage to target location\n" +
                "5. Manifest generation and verification"),
            createSection("Storage Targets",
                "• Local filesystem\n" +
                "• External devices\n" +
                "• SFTP servers"),
            createSection("Security Features",
                "• Encrypted backup archives\n" +
                "• SHA-256 integrity verification\n" +
                "• Password-based key derivation\n" +
                "• Manifest-based recovery"),
            createSection("Examples",
                "Initialize workspace:\n  backup init\n\n" +
                "Run a backup:\n  backup backup\n\n" +
                "Restore files:\n  backup restore\n\n" +
                "Check status:\n  backup status")
        );
        
        content.getChildren().addAll(headerBox, sections);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        return scrollPane;
    }
    
    private static VBox createSection(String title, String content) {
        VBox section = new VBox(10);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#3498db"));
        
        Label contentLabel = new Label(content);
        contentLabel.setTextFill(Color.web("#5d6d7e"));
        contentLabel.setWrapText(true);
        contentLabel.setLineSpacing(1.5);
        
        section.getChildren().addAll(titleLabel, contentLabel);
        return section;
    }
}
