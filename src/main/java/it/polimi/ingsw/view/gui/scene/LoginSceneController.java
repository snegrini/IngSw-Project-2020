package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginSceneController implements ViewGuiController {

    private View view;

    @FXML
    private Parent mainPane;
    @FXML
    private TextField nicknameField;

    @FXML
    private Button joinBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onJoinBtnClick(event));
        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBackBtnClick(event));
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    private void onJoinBtnClick(Event event) {
        String nickname = nicknameField.getText();

        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateNickname(nickname)));
    }

    private void onBackBtnClick(Event event) {
        SceneController.changeRootPane(view, event, "connect_scene.fxml");
    }
}
