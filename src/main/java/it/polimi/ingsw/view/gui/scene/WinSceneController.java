package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WinSceneController implements GenericSceneController {
    private final Stage stage;

    private double xOffset;
    private double yOffset;

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label titleLbl;
    @FXML
    private Label nicknameLbl;
    @FXML
    private Button okBtn;


    public WinSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
    }

    @FXML
    public void initialize() {
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    private void onRootPaneMousePressed(MouseEvent event) {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    private void onRootPaneMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    public void setWinnerNickname(String winnerNickname) {
        nicknameLbl.setText(winnerNickname);
    }

    public void displayWinScene() {
        stage.showAndWait();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }
}
