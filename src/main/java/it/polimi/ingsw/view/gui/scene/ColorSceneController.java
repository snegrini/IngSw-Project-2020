package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ColorSceneController extends ViewObservable implements GenericSceneController {

    private List<Color> availableColors;

    @FXML
    private ImageView blueWorker;
    @FXML
    private ImageView greenWorker;
    @FXML
    private ImageView redWorker;
    @FXML
    private Button backToMenuBtn;

    public ColorSceneController() {
        this.availableColors = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        blueWorker.setDisable(!availableColors.contains(Color.BLUE));
        greenWorker.setDisable(!availableColors.contains(Color.GREEN));
        redWorker.setDisable(!availableColors.contains(Color.RED));

        blueWorker.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Color.BLUE));
        greenWorker.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Color.GREEN));
        redWorker.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onWorkerClick(Color.RED));

        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    private void onWorkerClick(Color color) {
        blueWorker.setDisable(true);
        greenWorker.setDisable(true);
        redWorker.setDisable(true);
        new Thread(() -> notifyObserver(obs -> obs.onUpdateWorkersColor(color))).start();
    }

    private void onBackToMenuBtnClick(Event event) {
        notifyObserver(ViewObserver::onDisconnection);
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    public void setAvailableColors(List<Color> availableColors) {
        this.availableColors = availableColors;
    }
}
