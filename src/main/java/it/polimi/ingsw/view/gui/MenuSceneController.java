package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MenuSceneController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button playBtn;

    @FXML
    public void initialize() {
        playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onPlayButtonClick());
    }

    private void onPlayButtonClick() {
        // TODO do something. Can we switch scene here?
    }

}
