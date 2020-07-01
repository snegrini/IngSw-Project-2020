package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * This class implements the scene where players wait for other players to join the game.
 */
public class LobbySceneController extends ViewObservable implements GenericSceneController {

    private List<String> nicknames;
    private int maxPlayers;

    @FXML
    private Label playersLbl;
    @FXML
    private Label numbersLbl;
    @FXML
    private Button backToMenuBtn;

    @FXML
    public void initialize() {
        playersLbl.setText(String.join(", ", nicknames));
        numbersLbl.setText(nicknames.size() + "/" + maxPlayers);

        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    /**
     * Handle click on back to menu button.
     * @param event the mouse click event.
     */
    private void onBackToMenuBtnClick(Event event) {
        backToMenuBtn.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    /**
     * Set nicknames already logged in game.
     * @param nicknames list of nicknames.
     */
    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    /**
     * Set number of max players chosen by first user.
     * @param maxPlayers number of max players in game.
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Update nicknames and number of players connecte every time a user join.
     */
    public void updateValues() {
        playersLbl.setText(String.join(", ", this.nicknames));
        numbersLbl.setText(this.nicknames.size() + "/" + this.maxPlayers);
    }
}
