package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.gui.NavigationManager;

public class APP_GUI extends Application {

    @Override
    public void start(Stage stage) {
        NavigationManager.initialize(stage);
    }

    public static void launchFx(String[] args) {
        Application.launch(TestFx.class, args);
    }
}

