package main.gui.report;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.report.LogAnalyzer;
import main.report.LogAnalyzer.LogReport;
import main.report.LogAnalyzer.LogEntry;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class ReportView {
    
    public static ScrollPane getContent() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.getStyleClass().add("report-container");
        
        // generate full report
        LogReport report = LogAnalyzer.generateReport();
        
        // show header
        HBox header = createHeader(report);
        
        // build stat cards
        HBox statsSection = createStatsSection(report);
        
        // create chart section
        HBox chartsSection = createChartsSection(report);
        
        // create error analysis section
        VBox errorSection = createErrorSection(report);
        
        // create source analysis section
        VBox sourceSection = createSourceSection(report);
        
        // show resient activety
        VBox recentActivity = createRecentActivitySection(report);
        
        mainContainer.getChildren().addAll(
            header,
            statsSection,
            chartsSection,
            errorSection,
            sourceSection,
            recentActivity 
        );
        
        // using scrollpanel to create scrolling effect
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        return scrollPane;
    }
    
    private static HBox createHeader(LogReport report) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("System Report & Analytics");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50")); // for creating colors using web or css like  color formats.
        
        Label subtitle = new Label("Generated from backup system logs");
        subtitle.setTextFill(Color.web("#7f8c8d"));
        subtitle.setFont(Font.font(14));
        
        VBox textBox = new VBox(5, title, subtitle);
        
        Label dateLabel = new Label(getDateRange(report));
        dateLabel.setTextFill(Color.web("#3498db"));
        dateLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        header.getChildren().addAll(textBox, dateLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS); //can grow if extra space 
        
        return header;
    }
    
    private static String getDateRange(LogReport report) {
        if (report.getStartTime() == null || report.getEndTime() == null) {
            return "Date range: N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return String.format("%s - %s", 
            report.getStartTime().format(formatter),
            report.getEndTime().format(formatter));
    }
    
    private static HBox createStatsSection(LogReport report) {
        HBox statsContainer = new HBox(15);
        statsContainer.setAlignment(Pos.CENTER);
        
        VBox totalOpsCard = createStatCard("Total Operations", 
            String.valueOf(report.getTotalEntries()), "#3498db");
        
        VBox successCard = createStatCard("Success Rate", 
            String.format("%.1f%%", report.getSuccessRate()), "#2ecc71");
        
        VBox errorCard = createStatCard("Errors", 
            String.valueOf(report.getErrorCount()), "#e74c3c");
        
        VBox activeSourceCard = createStatCard("Most Active Source", 
            report.getMostActiveSource(), "#9b59b6");
        
        statsContainer.getChildren().addAll(totalOpsCard, successCard, errorCard, activeSourceCard);
        return statsContainer;
    }
    
    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(180);
        card.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-title");
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
        valueLabel.setTextFill(Color.web(color));
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    private static HBox createChartsSection(LogReport report) {
        HBox chartsContainer = new HBox(20);
        chartsContainer.setAlignment(Pos.CENTER);
        
        //Pie chart for status distribution
        VBox pieChartBox = new VBox(10);
        pieChartBox.setPadding(new Insets(20));
        pieChartBox.getStyleClass().add("chart-card");
        
        Label pieTitle = new Label("Status Distribution");
        pieTitle.getStyleClass().add("chart-title");
        
        PieChart statusChart = createStatusPieChart(report);
        statusChart.setPrefSize(300, 250);
        
        pieChartBox.getChildren().addAll(pieTitle, statusChart);
        
        // Bar chart for sources of activeties
        VBox barChartBox = new VBox(10);
        barChartBox.setPadding(new Insets(20));
        barChartBox.getStyleClass().add("chart-card");
        
        Label barTitle = new Label("Source Activity");
        barTitle.getStyleClass().add("chart-title");
        
        BarChart<String, Number> sourceChart = createSourceBarChart(report);
        sourceChart.setPrefSize(400, 250);
        
        barChartBox.getChildren().addAll(barTitle, sourceChart);
        
        chartsContainer.getChildren().addAll(pieChartBox, barChartBox);
        return chartsContainer;
    }
    
    private static PieChart createStatusPieChart(LogReport report) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Integer> entry : report.getStatusCount().entrySet()) {
            String status = entry.getKey();
            int count = entry.getValue();
            
            PieChart.Data data = new PieChart.Data(status + " (" + count + ")", count);
            pieChartData.add(data);
        }
        
        PieChart chart = new PieChart(pieChartData);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false);
        
        return chart;
    }
    
    private static BarChart<String, Number> createSourceBarChart(LogReport report) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Operations by Source");
        
        // get top 5 sources.
        report.getSourceCount().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> 
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()))
            );
        
        barChart.getData().add(series);
        barChart.setLegendVisible(false); // if set to true it addes addtional table of (color-status) referace for the chart
        
        return barChart;
    }
    
    private static VBox createErrorSection(LogReport report) {
        VBox errorContainer = new VBox(15);
        errorContainer.setPadding(new Insets(25));
        errorContainer.getStyleClass().add("section-card");
        
        Label title = new Label("Error Analysis");
        title.getStyleClass().add("section-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        if (report.getErrors().isEmpty()) {
            Label noErrors = new Label("No errors found in the logs.");
            noErrors.setTextFill(Color.web("#7f8c8d"));
            errorContainer.getChildren().addAll(title, noErrors);
            return errorContainer;
        }
        
        // error summery 
        HBox summaryBox = new HBox(20);
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        
        Label errorCountLabel = new Label("Total Errors: " + report.getErrorCount());
        errorCountLabel.setTextFill(Color.web("#e74c3c"));
        
        Label commonErrorLabel = new Label("Most Common: " + report.getMostCommonError());
        commonErrorLabel.setTextFill(Color.web("#7f8c8d"));
        
        summaryBox.getChildren().addAll(errorCountLabel, commonErrorLabel);
        
        // list all errors
        VBox errorList = new VBox(10);
        
        report.getErrors().stream()
            .limit(5)
            .forEach(error -> {
                HBox errorItem = new HBox(10);
                errorItem.setPadding(new Insets(10));
                errorItem.getStyleClass().add("error-item");
                
                Label sourceLabel = new Label(error.getSource());
                sourceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e74c3c;");
                
                Label messageLabel = new Label(error.getMessage());
                messageLabel.setTextFill(Color.web("#5d6d7e"));
                messageLabel.setWrapText(true);
                
                Label timeLabel = new Label(error.getTimestamp());
                timeLabel.setTextFill(Color.web("#95a5a6"));
                timeLabel.setFont(Font.font(12));
                
                errorItem.getChildren().addAll(sourceLabel, messageLabel, timeLabel);
                HBox.setHgrow(messageLabel, Priority.ALWAYS);
                
                errorList.getChildren().add(errorItem);
            });
        
        Button viewAllErrors = new Button("View All Errors");
        viewAllErrors.getStyleClass().add("view-all-btn");
        viewAllErrors.setOnAction(e -> showAllErrors(report));
        
        errorContainer.getChildren().addAll(title, summaryBox, errorList, viewAllErrors);
        return errorContainer;
    }
    
    private static VBox createSourceSection(LogReport report) {
        VBox sourceContainer = new VBox(15);
        sourceContainer.setPadding(new Insets(25));
        sourceContainer.getStyleClass().add("section-card");
        
        Label title = new Label("Source Breakdown");
        title.getStyleClass().add("section-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        TableView<SourceStat> sourceTable = new TableView<>();
        
        TableColumn<SourceStat, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().source));
        sourceCol.setPrefWidth(150);
        
        TableColumn<SourceStat, Integer> countCol = new TableColumn<>("Operations");
        countCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().count).asObject());
        countCol.setPrefWidth(100);
        
        TableColumn<SourceStat, String> percentageCol = new TableColumn<>("Percentage");
        percentageCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.1f%%", (data.getValue().count * 100.0) / report.getTotalEntries())
        ));
        percentageCol.setPrefWidth(100);
        
        sourceTable.getColumns().addAll(sourceCol, countCol, percentageCol);
        
       
        ObservableList<SourceStat> sourceData = FXCollections.observableArrayList();
        
        report.getSourceCount().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(entry -> sourceData.add(new SourceStat(entry.getKey(), entry.getValue())));
        
        sourceTable.setItems(sourceData);
        sourceTable.setPrefHeight(200);
        
        sourceContainer.getChildren().addAll(title, sourceTable);
        return sourceContainer;
    }
    
    private static class SourceStat {
        String source;
        int count;
        
        SourceStat(String source, int count) {
            this.source = source;
            this.count = count;
        }
    }
    
    private static VBox createRecentActivitySection(LogReport report) {
        VBox activityContainer = new VBox(15);
        activityContainer.setPadding(new Insets(25));
        activityContainer.getStyleClass().add("section-card");
        
        Label title = new Label("Recent Activity");
        title.getStyleClass().add("section-title");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        
        VBox activityList = new VBox(10);
        
        report.getEntries().stream()
            .skip(Math.max(0, report.getEntries().size() - 10))
            .forEach(entry -> {
                HBox activityItem = new HBox(10);
                activityItem.setPadding(new Insets(8));
                activityItem.getStyleClass().add("activity-item");
                
                VBox details = new VBox(3);
                
                HBox header = new HBox(10);
                header.setAlignment(Pos.CENTER_LEFT);
                
                Label sourceLabel = new Label(entry.getSource());
                sourceLabel.setStyle("-fx-font-weight: bold;");
                
                Label timeLabel = new Label(entry.getTimestamp());
                timeLabel.setTextFill(Color.web("#7f8c8d"));
                timeLabel.setFont(Font.font(12));
                
                header.getChildren().addAll(sourceLabel, timeLabel);
                
                Label messageLabel = new Label(entry.getMessage());
                messageLabel.setTextFill(Color.web("#5d6d7e"));
                messageLabel.setWrapText(true);
                
                details.getChildren().addAll(header, messageLabel);
                HBox.setHgrow(details, Priority.ALWAYS);
                
                activityItem.getChildren().addAll(details);
                activityList.getChildren().add(activityItem);
            });
        
        activityContainer.getChildren().addAll(title, activityList);
        return activityContainer;
    }
    
    
    private static void showAllErrors(LogReport report) {
        Stage errorStage = new Stage();
        errorStage.setTitle("All System Errors");
        
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        
        Label title = new Label("All Error Logs (" + report.getErrorCount() + " errors)");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        TableView<ErrorDetail> errorTable = new TableView<>();
        
        TableColumn<ErrorDetail, String> sourceCol = new TableColumn<>("Source");
        sourceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().source));
        sourceCol.setPrefWidth(150);
        
        TableColumn<ErrorDetail, String> messageCol = new TableColumn<>("Error Message");
        messageCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().message));
        messageCol.setPrefWidth(400);
        
        TableColumn<ErrorDetail, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().timestamp));
        timeCol.setPrefWidth(150);
        
        errorTable.getColumns().addAll(sourceCol, messageCol, timeCol);
        
        ObservableList<ErrorDetail> errorData = FXCollections.observableArrayList();
        report.getErrors().forEach(error -> 
            errorData.add(new ErrorDetail(error.getSource(), error.getMessage(), error.getTimestamp()))
        );
        
        errorTable.setItems(errorData);
        
        container.getChildren().addAll(title, errorTable);
        Scene scene = new Scene(new ScrollPane(container), 800, 600);
        errorStage.setScene(scene);
        errorStage.show();
    }
    
    private static class ErrorDetail {
        String source;
        String message;
        String timestamp;
        
        ErrorDetail(String source, String message, String timestamp) {
            this.source = source;
            this.message = message;
            this.timestamp = timestamp;
        }
    }
    
}
