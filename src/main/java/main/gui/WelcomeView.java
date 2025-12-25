package main.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import main.report.LogAnalyzer;
import main.report.LogAnalyzer.LogReport;

public class WelcomeView {
    
    public static VBox getContent() {
        LogReport report = LogAnalyzer.generateReport();
        
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        
        Label title = new Label("Backup System Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        HBox statsSection = createStatsSection(report);
        
        VBox recentActivity = createRecentActivity(report);
        
        mainContainer.getChildren().addAll(title, statsSection, recentActivity);
        return mainContainer;
    }
    
    private static HBox createStatsSection(LogReport report) {
        HBox statsContainer = new HBox(15);
        statsContainer.setAlignment(Pos.CENTER);
        
        VBox totalOps = createStatCard("Total Operations", 
            String.valueOf(report.getTotalEntries()));
        VBox successCard = createStatCard("Success Rate", 
            String.format("%.1f%%", report.getSuccessRate()));
        VBox errorCard = createStatCard("Errors", 
            String.valueOf(report.getErrorCount()));
        VBox activeSource = createStatCard("Active Source", 
            report.getMostActiveSource());
        
        statsContainer.getChildren().addAll(totalOps, successCard, errorCard, activeSource);
        return statsContainer;
    }
    
    private static VBox createStatCard(String title, String value) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        card.setPrefWidth(150);
        card.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    private static VBox createRecentActivity(LogReport report) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8px; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label sectionTitle = new Label("Recent Activity");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        VBox activityList = new VBox(8);
        
        int startIndex = Math.max(0, report.getEntries().size() - 5);
        for (int i = startIndex; i < report.getEntries().size(); i++) {
            var entry = report.getEntries().get(i);
            Label activity = new Label(String.format("[%s] %s - %s", 
                entry.getTimestamp(), entry.getSource(), entry.getMessage()));
            activity.setWrapText(true);
            activity.setStyle("-fx-font-size: 13px; -fx-text-fill: #5d6d7e;");
            activityList.getChildren().add(activity);
        }
        
        container.getChildren().addAll(sectionTitle, activityList);
        return container;
    }
}
