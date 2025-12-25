package main.gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import utils.manifest.ManifestEntry;
import utils.manifest.ManifestDisplay;
import java.util.List;

public class StatusView {
    
    public static VBox getContent() {
        VBox content = new VBox(10);
        content.setPadding(new javafx.geometry.Insets(20));
        
        Label title = new Label("Backup Status");
        Label subtitle = new Label("View all backed up files and their details");
        
        TableView<ManifestEntry> table = createStatusTable();
        content.getChildren().addAll(title, subtitle, table);
        return content;
    }
    
    public static TableView<ManifestEntry> createStatusTable() {
        return createTableView();
    }
    
    public static TableView<ManifestEntry> createSelectiveRecoveryTable() {
        TableView<ManifestEntry> table = createTableView();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return table;
    }
    
    private static TableView<ManifestEntry> createTableView() {
        List<ManifestEntry> manifestList = ManifestDisplay.getEntries();
        ObservableList<ManifestEntry> data = FXCollections.observableArrayList(manifestList);
        
        TableView<ManifestEntry> table = new TableView<>(data);
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<ManifestEntry, String> fileCol = new TableColumn<>("File Name");
        fileCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().original().toString()));
        
        TableColumn<ManifestEntry, String> hashCol = new TableColumn<>("Hash");
        hashCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().hash()));
        
        TableColumn<ManifestEntry, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(c -> new SimpleStringProperty(
            String.valueOf(c.getValue().originalSize()) + " bytes"
        ));
        
        TableColumn<ManifestEntry, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().at()));
        
        table.getColumns().addAll(fileCol, hashCol, sizeCol, dateCol);
        table.setPrefWidth(1000);
        table.setPrefHeight(500);
        
        return table;
    }
}
