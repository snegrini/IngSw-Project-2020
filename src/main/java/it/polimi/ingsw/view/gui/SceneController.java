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

    /**
     * Changes the root panel of the scene argument.
     *
     * @param view  the view to be set into the FXMLLoader controller.
     * @param scene the scene whose change the root panel.
     * @param fxml  the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
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

    /**
     * Changes the root panel of the scene argument.
     *
     * @param view  the view to be set into the FXMLLoader controller.
     * @param event the event which is happened into the scene.
     * @param fxml  the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public static ViewGuiController changeRootPane(View view, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(view, scene, fxml);
    }

    /**
     * Changes the root panel of the scene argument.
     * Offers the possibility to set a custom controller to the FXMLLoader.
     *
     * @param controller the custom controller that will be set into the FXMLLoader.
     * @param scene      the scene whose change the panel.
     * @param fxml       the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     */
    public static void changeRootPane(ViewGuiController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));

            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            Parent root = loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * Changes the root panel of the scene argument.
     * Offers the possibility to set a custom controller to the FXMLLoader.
     *
     * @param controller the custom controller that will be set into the FXMLLoader.
     * @param event      the event which is happened into the scene.
     * @param fxml       the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     */
    public static void changeRootPane(ViewGuiController controller, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        changeRootPane(controller, scene, fxml);
    }

    /**
     * Shows a custom message in a popup.
     *
     * @param title   the title of the popup.
     * @param message the message of the popup.
     */
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
