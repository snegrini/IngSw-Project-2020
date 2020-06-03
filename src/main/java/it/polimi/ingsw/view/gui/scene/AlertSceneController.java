package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertSceneController implements GenericSceneController {

    private final Stage stage;

    @FXML
    private Label titleLbl;
    @FXML
    private Label messageLbl;
    @FXML
    private Button okBtn;


    public AlertSceneController() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
    }

    @FXML
    public void initialize() {
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    public void setAlertTitle(String str) {
        titleLbl.setText(str);
    }

    public void setAlertMessage(String str) {
        messageLbl.setText(str);
    }

    public void displayAlert() {
        stage.showAndWait();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }
}
