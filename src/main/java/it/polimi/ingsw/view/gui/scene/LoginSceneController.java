package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
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
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinBtnClick);
        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    private void onJoinBtnClick(Event event) {
        joinBtn.setDisable(true);

        String nickname = nicknameField.getText();

        new Thread(() -> notifyObserver(obs -> obs.onUpdateNickname(nickname))).start();
    }

    private void onBackToMenuBtnClick(Event event) {
        notifyObserver(ViewObserver::onDisconnection);
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }
}
