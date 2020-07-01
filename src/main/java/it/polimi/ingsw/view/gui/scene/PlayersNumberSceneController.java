package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

/**
 * This class implements the scene where the game host chooses the number of players who are going to play.
 */
public class PlayersNumberSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private Button confirmBtn;
    @FXML
    private Button backToMenuBtn;

    @FXML
    private RadioButton radioBtn1;
    @FXML
    private RadioButton radioBtn2;
    @FXML
    private ToggleGroup toggleGroup;

    private int minPlayers;
    private int maxPlayers;

    /**
     * Default constructor.
     */
    public PlayersNumberSceneController() {
        minPlayers = 0;
        maxPlayers = 0;
    }

    @FXML
    public void initialize() {
        radioBtn1.setText(minPlayers + " players");
        radioBtn2.setText(maxPlayers + " players");

        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);
        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    /**
     * Handles click on Confirm button.
     *
     * @param event the mouse click event.
     */
    private void onConfirmBtnClick(Event event) {
        confirmBtn.setDisable(true);
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        int playersNumber = Character.getNumericValue(selectedRadioButton.getText().charAt(0));

        new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(playersNumber))).start();
    }

    /**
     * Handles click on Back button
     *
     * @param event the mouse click event.
     */
    private void onBackToMenuBtnClick(Event event) {
        backToMenuBtn.setDisable(true);
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }

    /**
     * Initialises the values for the radioButtons.
     *
     * @param minPlayers the minimum number of players.
     * @param maxPlayers the maximum number of players.
     */
    public void setPlayersRange(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }
}
