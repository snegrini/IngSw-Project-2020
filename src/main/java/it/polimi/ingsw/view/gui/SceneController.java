package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scene.AlertSceneController;
import it.polimi.ingsw.view.gui.scene.ViewGuiController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Logger;

public class SceneController {

    public static final Logger LOGGER = Logger.getLogger(SceneController.class.getName());

    public static ViewGuiController changeRootPane(View view, Scene scene, String fxml) {
        ViewGuiController controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setView(view);

            scene.setRoot(root);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return controller;
    }

    public static ViewGuiController changeRootPane(View view, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(view, scene, fxml);
    }

    public static void showAlert(String title, String message) {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/alert_scene.fxml"));

        Parent parent;
        try {
            parent = loader.load();
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            return;
        }
        AlertSceneController alertSceneController = loader.getController();
        Scene alertScene = new Scene(parent);
        alertSceneController.setScene(alertScene);
        alertSceneController.setAlertTitle(title);
        alertSceneController.setAlertMessage(message);
        alertSceneController.displayAlert();
    }



}
