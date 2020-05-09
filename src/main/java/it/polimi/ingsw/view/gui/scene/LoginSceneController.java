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

import java.util.Map;

public class LoginSceneController implements ViewGuiController {

    private View view;

    @FXML
    private Parent mainPane;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField serverAddressField;
    @FXML
    private TextField serverPortField;

    @FXML
    private Button connectBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        connectBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectButtonClick(event));
        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBackButtonClick(event));
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    private void onConnectButtonClick(Event event) {
        String address = serverAddressField.getText();
        String port = serverPortField.getText();
        // TODO check input

        Map<String, String> serverInfo = Map.of("address", address, "port", port);
        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo)));

        // Stop if connection is not set.


        String nickname = nicknameField.getText();
        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateNickname(nickname)));

        // TODO change pane. Ask player numbers and wait for other players
        //      Can I set the player numbers from the login scene?
    }

    private void onBackButtonClick(Event event) {
        SceneController.changeRootPane(view, event, "menu_scene.fxml");
    }
}
