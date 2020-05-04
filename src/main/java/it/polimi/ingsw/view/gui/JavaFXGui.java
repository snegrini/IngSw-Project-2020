package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class JavaFXGui extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Pane()));

        stage.show();
    }

}
