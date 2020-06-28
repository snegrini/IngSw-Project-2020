package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class implements the controller of a generic Alert Scene.
 */
public class AlertSceneController implements GenericSceneController {

    private final Stage stage;

    @FXML
    private Label titleLbl;
    @FXML
    private Label messageLbl;
    @FXML
    private Button okBtn;

    /**
     * Default constructor.
     */
    public AlertSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
    }

    @FXML
    public void initialize() {
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    /**
     * Handle the click on the Ok button.
     * An alert message will be shown.
     *
     * @param event the mouse click event.
     */
    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    /**
     * Set the title of the Alert Scene.
     * @param str title of the Alert Scene.
     */
    public void setAlertTitle(String str) {
        titleLbl.setText(str);
    }

    /**
     * Set the message of the Alert Scene.
     * @param str message of the Alert Scene.
     */
    public void setAlertMessage(String str) {
        messageLbl.setText(str);
    }

    /**
     * Display Alert Message Pop-Up
     */
    public void displayAlert() {
        stage.showAndWait();
    }

    /**
     * Set the scene of the stage.
     * @param scene scene to be setted.
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }
}
