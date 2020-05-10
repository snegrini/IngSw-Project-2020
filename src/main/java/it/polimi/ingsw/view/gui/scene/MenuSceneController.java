package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MenuSceneController implements ViewGuiController {
    private View view;

    @FXML
    private GridPane gridPane;
    @FXML
    private Button playBtn;
    @FXML
    private Button quitBtn;

    @FXML
    public void initialize() {
        playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onPlayButtonClick(event));
        quitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    private void onPlayButtonClick(Event event) {
        SceneController.changeRootPane(view, event, "login_scene.fxml");
    }

}
