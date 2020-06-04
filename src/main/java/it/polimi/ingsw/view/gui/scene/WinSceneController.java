package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WinSceneController implements GenericSceneController  {
    private final Stage stage;

    @FXML
    private Label titleLbl;
    @FXML
    private Label nicknameLbl;
    @FXML
    private Button okBtn;


    public WinSceneController() {
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

        // TODO Close all and disconnect.
    }

    public void setNicknameLbl(String str) {
        nicknameLbl.setText(str);
    }

    public void displayAlert() {
        stage.showAndWait();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }
}
