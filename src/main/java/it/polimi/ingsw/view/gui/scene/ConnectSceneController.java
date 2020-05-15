package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.controller.ClientController;
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

public class ConnectSceneController implements ViewGuiController {

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
        connectBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConnectBtnClick(event));
        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBackBtnClick(event));
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    private void onConnectBtnClick(Event event) {
        String address = serverAddressField.getText();
        String port = serverPortField.getText();

        // TODO input check
        ClientController.isValidIpAddress(address);
        ClientController.isValidPort(Integer.parseInt(port));

        Map<String, String> serverInfo = Map.of("address", address, "port", port);
        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo)));
    }

    private void onBackBtnClick(Event event) {
        SceneController.changeRootPane(view, event, "menu_scene.fxml");
    }
}
