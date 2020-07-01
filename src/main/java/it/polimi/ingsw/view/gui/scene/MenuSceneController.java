package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * This class implements the main menu scene.
 */
public class MenuSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button playBtn;
    @FXML
    private Button quitBtn;

    @FXML
    public void initialize() {
        playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPlayBtnClick);
        quitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }

    /**
     * Handles click on Play button.
     *
     * @param event the mouse click event.
     */
    private void onPlayBtnClick(Event event) {
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");
    }
}
