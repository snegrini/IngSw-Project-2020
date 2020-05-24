package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private TextField nicknameField;

    @FXML
    private Button joinBtn;
    @FXML
    private Button backToMenuBtn;

    @FXML
    public void initialize() {
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onJoinBtnClick(event));
        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBackToMenuBtnClick(event));
    }

    private void onJoinBtnClick(Event event) {
        String nickname = nicknameField.getText();

        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateNickname(nickname)));
    }

    private void onBackToMenuBtnClick(Event event) {
        // TODO disconnect from server
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");
    }
}
