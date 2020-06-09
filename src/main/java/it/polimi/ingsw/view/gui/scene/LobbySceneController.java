package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.List;

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

    private void onBackToMenuBtnClick(Event event) {
        notifyObserver(obs -> obs.onDisconnection());
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void updateValues() {
        playersLbl.setText(String.join(", ", this.nicknames));
        numbersLbl.setText(this.nicknames.size() + "/" + this.maxPlayers);
    }
}
