package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.gui.scene.MenuSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;


public class JavaFXGui extends Application {

    @Override
    public void start(Stage stage) {
        Gui view = new Gui();
        ClientController clientController = new ClientController(view);
        view.addObserver(clientController);

        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/menu_scene.fxml"));
        Parent rootLayout = null;
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            SceneController.LOGGER.severe(e.getMessage());
        }
        MenuSceneController controller = loader.getController();
        controller.addObserver(clientController);

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.setWidth(1300d);
        stage.setHeight(760d);
        stage.setResizable(false);
        //stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle("Santorini Board Game");
        stage.show();
    }
}
