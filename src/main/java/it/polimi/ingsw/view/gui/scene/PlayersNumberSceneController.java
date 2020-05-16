package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class PlayersNumberSceneController implements ViewGuiController {
    private View view;

    @FXML
    private Button confirmBtn;
    @FXML
    private Button mainMenuBtn;

    @FXML
    private RadioButton radioBtn1;
    @FXML
    private RadioButton radioBtn2;
    @FXML
    private ToggleGroup toggleGroup;

    private int minPlayers;
    private int maxPlayers;

    public PlayersNumberSceneController() {
        minPlayers = 0;
        maxPlayers = 0;
    }

    @FXML
    public void initialize() {
        radioBtn1.setText(minPlayers + " players");
        radioBtn2.setText(maxPlayers + " players");

        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onConfirmBtnClick(event));
        mainMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onMainMenuBtnClick(event));
    }

    private void onConfirmBtnClick(Event event) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        int playersNumber = Character.getNumericValue(selectedRadioButton.getText().charAt(0));

        Platform.runLater(() -> view.notifyObserver(obs -> obs.onUpdatePlayersNumber(playersNumber)));
    }

    private void onMainMenuBtnClick(Event event) {
        // TODO disconnect
        SceneController.changeRootPane(view, event, "menu_scene.fxml");
    }

    @Override
    public void setView(View view) {
        this.view = view;
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
