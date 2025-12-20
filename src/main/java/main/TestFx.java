package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.gui.Welcome;
import main.gui.Status;

public class TestFx extends Application {

    @Override
    public void start(Stage primaryStage) {

        // show welcome
        Stage welcomeStage = Welcome.getWelcomeStage();
        welcomeStage.show();


        //show status (optional)
        //Stage statusStage = Status.getStatusStage();
        //statusStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

