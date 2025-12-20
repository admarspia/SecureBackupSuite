package main.gui;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

public class Welcome {

    public static Stage getWelcomeStage() {

        BorderPane root = new BorderPane();
      //  root.setPadding(new Insets(20));

        Label title = new Label("BACKUP SYSTEM");

        Label subtitle = new Label(
                "Secure, modular backup application for protecting critical data."
        );

        title.getStyleClass().add("title");
        subtitle.getStyleClass().add("subtitle");

        VBox headerMsg = new VBox(20, title, subtitle);
        headerMsg.setPadding(new Insets(10));



        VBox header = new VBox(10, Header.getHeader(), headerMsg);

        VBox overview = new VBox(5,
                new Label("Backup Pipeline"),
                new Label("• File discovery"),
                new Label("• Compression"),
                new Label("• Encryption"),
                new Label("• Local or remote storage")
        );

        VBox storage = new VBox(5,
                new Label("Supported Storage"),
                new Label("• Local filesystem"),
                new Label("• SFTP remote servers")
        );

        VBox security = new VBox(5,
                new Label("Security"),
                new Label("• Encrypted backup archives"),
                new Label("• SHA-256 file integrity verification"),
                new Label("• Manifest-based tracking and recovery")
        );

        GridPane utils = new GridPane();
        utils.setHgap(20);
        utils.setVgap(20);
        utils.setPadding(new Insets(10));
        utils.add(overview, 0, 0);
        utils.add(storage, 1, 0);
        utils.add(security, 0, 1);
        utils.add(new VBox(5,
                new Label("Workspace"),
                new Label("• ./backup_workspace/")
        ), 1, 1);

        Button btnUsage = new Button("Usage");
        Button btnCancel = new Button("Cancel");

        HBox controls = new HBox(10, btnUsage, btnCancel);

        root.setTop(header);
        root.setCenter(utils);

        utils.getStyleClass().add("card");

        
        root.setBottom(controls);
        
        controls.getStyleClass().add("card");

        Scene scene = new Scene(root, 800, 600);

        scene.getStylesheets().add(
            Welcome.class.getResource("/styles/app.css").toExternalForm()
        );


        Stage stage = new Stage();
        stage.setTitle("Welcome to SecureBackupSuite");
        stage.setResizable(false);
        stage.setScene(scene);

        AppIcon.apply(stage);


        btnCancel.setOnAction(new HandleClose(stage));
        btnUsage.setOnAction(e -> HelpStage.showHelp());

        return stage;
    }
}

