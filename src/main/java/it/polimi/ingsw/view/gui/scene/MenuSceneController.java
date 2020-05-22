package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MenuSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button playBtn;
    @FXML
    private Button quitBtn;

    @FXML
    public void initialize() {
        playBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onPlayBtnClick(event));
        quitBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }

    private void onPlayBtnClick(Event event) {
        SceneController.changeRootPane(observers, event, "connect_scene.fxml");

        /*List<Position> spacesToBeEnabled = List.of(new Position(0, 1), new Position(1, 0), new Position(1, 1));
        BoardSceneController bsc = new BoardSceneController();
        bsc.addAllObservers(observers);
        SceneController.changeRootPane(bsc, event, "board_scene.fxml");
        bsc.setEnabledSpaces(spacesToBeEnabled);*/
    }
}
