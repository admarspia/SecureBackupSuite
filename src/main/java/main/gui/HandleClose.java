package main.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class HandleClose implements EventHandler<ActionEvent> {

    private final Stage stage;

    public HandleClose(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        if (stage.isShowing()) {
            stage.close();
        }
    }
}

