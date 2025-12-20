package main.gui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppIcon {
    private static final Image ICON =
        new Image(AppIcon.class.getResource("/10109861.png").toExternalForm());

    public static void apply(Stage stage) {
        stage.getIcons().add(ICON);
    }
}

