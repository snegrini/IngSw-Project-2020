package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class ColorSceneController implements ViewGuiController {
    private View view;

    private List<Color> availableColors;

    @FXML
    private ImageView blueWorker;
    @FXML
    private ImageView greenWorker;
    @FXML
    private ImageView redWorker;
    @FXML
    private Button mainMenuBtn;

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

        mainMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onBackBtnClick(event));
    }

    private void onWorkerClick(Color color) {
        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdateWorkersColor(color)));
    }

    private void onBackBtnClick(Event event) {
        SceneController.changeRootPane(view, event, "menu_scene.fxml");
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    public void setAvailableColors(List<Color> availableColors) {
        this.availableColors = availableColors;
    }
}
