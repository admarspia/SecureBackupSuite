package main.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NavigationManager {
    private static Stage primaryStage;
    private static BorderPane rootLayout;
    
    public static void initialize(Stage stage) {
        primaryStage = stage;
        rootLayout = new BorderPane();
        
        rootLayout.setTop(Header.getHeader());
        
        Scene mainScene = new Scene(rootLayout, 1200, 700);
        mainScene.getStylesheets().add(
            NavigationManager.class.getResource("/styles/app.css").toExternalForm() // to converts the URL object into a standard string representation
        );
        
        stage.setScene(mainScene);
        stage.setTitle("Backup System");
        AppIcon.apply(stage);
        stage.show();
        
        navigateToWelcome();
    }
    
    // Add report navigation
    public static void navigateToReport() {
        rootLayout.setCenter(main.gui.report.ReportView.getContent());
        primaryStage.setTitle("System Report - Backup System");
    }
    
    public static void navigateToWelcome() {
        rootLayout.setCenter(WelcomeView.getContent());
        primaryStage.setTitle("Welcome - Backup System");
    }
    
    public static void navigateToStatus() {
        rootLayout.setCenter(StatusView.getContent());
        primaryStage.setTitle("Status - Backup System");
    }
    
    public static void navigateToLogs() {
        rootLayout.setCenter(LogView.getContent());
        primaryStage.setTitle("Logs - Backup System");
    }
    
    public static void navigateToHelp() {
        rootLayout.setCenter(HelpView.getContent());
        primaryStage.setTitle("Help - Backup System");
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static BorderPane getRootLayout() {
        return rootLayout;
    }
}
