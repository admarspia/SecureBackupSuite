package main.gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;

import java.util.List;
import utils.manifest.*;

public class Status {

    public static TableView<ManifestEntry> getStatusTable() {
        List<ManifestEntry> manifestList = ManifestDisplay.getEntries();
        ObservableList<ManifestEntry> data = FXCollections.observableArrayList(manifestList);

        TableView<ManifestEntry> table = new TableView<>(data);

        TableColumn<ManifestEntry, String> fileCol =
                new TableColumn<>("File Name");
        fileCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().original().toString()));

        TableColumn<ManifestEntry, String> hashCol =
                new TableColumn<>("Hash");
        hashCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().hash()));

        TableColumn<ManifestEntry, String> sizeCol =
                new TableColumn<>("Size");
        sizeCol.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().originalSize())));

        TableColumn<ManifestEntry, String> dateCol =
                new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().at()));

        table.getColumns().addAll(fileCol, hashCol, sizeCol, dateCol);

        table.setPrefWidth(1000);
        table.setPrefHeight(600);

        return table;
    }

    public static Stage getStatusStage() {
        TableView<ManifestEntry> table = Status.getStatusTable();

        // Main layout
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(20));

        // Top header
        root.setTop(Header.getHeader());

        // Center content
        VBox content = new VBox(10);
        content.getStyleClass().add("card");

        Label statusMsg = new Label("Your backup status.");
        CheckBox rememberBox = new CheckBox("Remember choice");
        Pane tableWrapper = new Pane(table);

        content.getChildren().addAll(statusMsg , tableWrapper);
        root.setCenter(content);

        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add(
                Status.class.getResource("/styles/app.css").toExternalForm()
                );

        Stage stage = new Stage();
        stage.setTitle("Backup Status");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(600);

        AppIcon.apply(stage);


        return stage;
    }
}

