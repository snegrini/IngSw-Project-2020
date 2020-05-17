package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class LobbySceneController implements ViewGuiController {
    private View view;
    private List<String> nicknames;
    private int maxPlayers;

    @FXML
    private Label playersLbl;
    @FXML
    private Label numbersLbl;
    @FXML
    private Button mainMenuBtn;

    @FXML
    public void initialize() {
        playersLbl.setText(String.join(", ", nicknames));
        numbersLbl.setText(nicknames.size() + "/" + maxPlayers);

        mainMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMainMenuBtnClick(event));
    }

    private void onMainMenuBtnClick(Event event) {
        // TODO disconnect
        SceneController.changeRootPane(view, event, "menu_scene.fxml");
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
}
