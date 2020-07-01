package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Map;

/**
 * This class implements the scene where users connect to the server.
 */
public class ConnectSceneController extends ViewObservable implements GenericSceneController {

    private final PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    @FXML
    private Parent rootPane;

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
        connectBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectBtnClick);
        backBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackBtnClick);
    }

    /**
     * Handle the click on the connect button.
     *
     * @param event the mouse click event.
     */
    private void onConnectBtnClick(Event event) {
        String address = serverAddressField.getText();
        String port = serverPortField.getText();

        boolean isValidIpAddress = ClientController.isValidIpAddress(address);
        boolean isValidPort = ClientController.isValidPort(port);

        serverAddressField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValidIpAddress);
        serverPortField.pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !isValidPort);

        if (isValidIpAddress && isValidPort) {
            backBtn.setDisable(true);
            connectBtn.setDisable(true);

            Map<String, String> serverInfo = Map.of("address", address, "port", port);
            new Thread(() -> notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo))).start();
        }
    }

    /**
     * Handle the click on the back button.
     *
     * @param event the mouse click event.
     */
    private void onBackBtnClick(Event event) {
        backBtn.setDisable(true);
        connectBtn.setDisable(true);

        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }
}
