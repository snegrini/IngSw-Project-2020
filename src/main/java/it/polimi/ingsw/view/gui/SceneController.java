package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.scene.AlertSceneController;
import it.polimi.ingsw.view.gui.scene.GenericSceneController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SceneController extends ViewObservable {

    public static final Logger LOGGER = Logger.getLogger(SceneController.class.getName());

    private static Scene activeScene;

    private static GenericSceneController activeController;

    public static Scene getActiveScene() {
        return activeScene;
    }

    public static GenericSceneController getActiveController() {
        return activeController;
    }

    public static ReducedGod god;

    /**
     * Changes the root panel of the scene argument.
     *
     * @param observerList a list of observers to be set into the scene controller.
     * @param scene        the scene whose change the root panel. This will become the active scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, Scene scene, String fxml) {
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observerList);

            activeController = (GenericSceneController) controller;
            activeScene = scene;
            activeScene.setRoot(root);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        return controller;
    }

    /**
     * Changes the root panel of the scene argument.
     *
     * @param observerList a list of observers to be set into the scene controller.
     * @param event        the event which is happened into the scene.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        return changeRootPane(observerList, scene, fxml);
    }

    /**
     * Changes the root panel of the active scene.
     *
     * @param observerList a list of observers to be set into the scene controller.
     * @param fxml         the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     * @return the controller of the new scene loaded by the FXMLLoader.
     */
    public static <T> T changeRootPane(List<ViewObserver> observerList, String fxml) {
        return changeRootPane(observerList, activeScene, fxml);
    }

    /**
     * Changes the root panel of the scene argument.
     * Offers the possibility to set a custom controller to the FXMLLoader.
     *
     * @param controller the custom controller that will be set into the FXMLLoader.
     * @param scene      the scene whose change the root panel. This will become the active scene.
     * @param fxml       the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     */
    public static void changeRootPane(GenericSceneController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));

            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            activeController = controller;
            Parent root = loader.load();

            activeScene = scene;
            activeScene.setRoot(root);
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
    public static void changeRootPane(GenericSceneController controller, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        changeRootPane(controller, scene, fxml);
    }

    /**
     * Changes the root panel of the active scene.
     * Offers the possibility to set a custom controller to the FXMLLoader.
     *
     * @param controller the custom controller that will be set into the FXMLLoader.
     * @param fxml       the new scene fxml name. It must include the extension ".fxml" (i.e. next_scene.fxml).
     */
    public static void changeRootPane(GenericSceneController controller, String fxml) {
        changeRootPane(controller, activeScene, fxml);
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

    /**
     * Set God picked from user.
     * @param pickedGod selected from user.
     */
    public static void setGod(ReducedGod pickedGod) {
        god = pickedGod;
    }

    public static ReducedGod getGod() {
        return god;
    }
}
