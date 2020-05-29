package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GodsSceneController extends ViewObservable implements GenericSceneController {

    private List<ReducedGod> gods;
    private int numberRequest;

    private int godIndex;
    private List<ReducedGod> selectedGods;

    @FXML
    private Button prevGodBtn;
    @FXML
    private Button nextGodBtn;
    @FXML
    private Button selectGodBtn;
    @FXML
    private Button deselectGodBtn;
    @FXML
    private Button confirmBtn;
    @FXML
    private ImageView focusGodImg;
    @FXML
    private ListView<String> selectedGodsListView;

    public GodsSceneController() {
        godIndex = 0;
        selectedGods = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        setFocusGodImage(gods.get(0).getName());

        deselectGodBtn.setDisable(true);

        checkAndDisableButton(prevGodBtn, 0);
        checkAndDisableButton(nextGodBtn, gods.size() - 1);

        prevGodBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onPrevGodBtnClick);
        nextGodBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onNextGodBtnClick);
        selectGodBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectGodBtnClick);
        deselectGodBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onDeselectGodBtnClick);
        confirmBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmBtnClick);

        focusGodImg.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onFocusGodImgClick);

    }

    private void onFocusGodImgClick(Event event) {
        ReducedGod god = gods.get(godIndex);
        SceneController.showGodInformation(god.getName(), god.getCaption(), god.getDescription());
    }

    /**
     * Disables the given button if the godIndex is equal to the number argument.
     * Enables the button if the condition is not satisfied.
     *
     * @param button the button to be disabled.
     * @param number the number to be compared.
     * @return {@code true} if the button has been disabled, {@code false} otherwise.
     */
    private boolean checkAndDisableButton(Button button, int number) {
        if (godIndex == number) {
            button.setDisable(true);
            return true;
        }
        button.setDisable(false);
        return false;
    }

    private void setFocusGodImage(String godName) {
        Image img = new Image(getClass().getResourceAsStream("/images/cards/" + godName.toLowerCase() + ".png"));
        focusGodImg.setImage(img);
    }

    private void setFocusGodImage() {
        setFocusGodImage(gods.get(godIndex).getName());
    }

    private void onPrevGodBtnClick(Event event) {

        if (godIndex > 0) {
            godIndex--;
            nextGodBtn.setDisable(false);
        }
        checkAndDisableButton(prevGodBtn, 0);
        checkSelectButtonsStatus();

        Platform.runLater(this::setFocusGodImage);
    }

    private void onNextGodBtnClick(Event event) {

        if (godIndex < gods.size() - 1) {
            godIndex++;
            prevGodBtn.setDisable(false);
        }
        checkAndDisableButton(nextGodBtn, gods.size() - 1);
        checkSelectButtonsStatus();

        Platform.runLater(this::setFocusGodImage);
    }

    private void onSelectGodBtnClick(Event event) {
        selectedGods.add(gods.get(godIndex));

        checkSelectButtonsStatus();
        updateSelectedGodsListView();
    }

    private void onDeselectGodBtnClick(MouseEvent event) {
        selectedGods.remove(gods.get(godIndex));
        checkSelectButtonsStatus();
        updateSelectedGodsListView();
    }

    private void onConfirmBtnClick(Event event) {

        if (selectedGods.size() < numberRequest) {
            SceneController.showAlert("Error", "Select exactly " + numberRequest + (numberRequest == 1 ? " god!" : " gods!"));
        } else {
            disableAllButtons();
            Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateGod(selectedGods)));
        }
    }

    /**
     * Disable all the buttons.
     */
    private void disableAllButtons() {
        prevGodBtn.setDisable(true);
        nextGodBtn.setDisable(true);
        deselectGodBtn.setDisable(true);
        selectGodBtn.setDisable(true);
        confirmBtn.setDisable(true);
    }

    /**
     * Checks and inverts the current status of the selected and unselected buttons.
     */
    private void checkSelectButtonsStatus() {

        if (selectedGods.size() != numberRequest && selectGodBtn.isDisable()) {
            selectGodBtn.setDisable(false);
        }
        if (selectedGods.contains(gods.get(godIndex))) {
            selectGodBtn.setDisable(true);
            deselectGodBtn.setDisable(false);
        } else {
            if (selectedGods.size() == numberRequest) {
                selectGodBtn.setDisable(true);
            } else {
                selectGodBtn.setDisable(false);
            }
            deselectGodBtn.setDisable(true);
        }

    }

    /**
     * Updates the selected gods list view.
     */
    private void updateSelectedGodsListView() {
        List<String> godNameList = selectedGods.stream()
                .map(god -> god.getName())
                .collect(Collectors.toList());

        selectedGodsListView.setItems(FXCollections.observableArrayList(godNameList));
    }

    public void setGods(List<ReducedGod> gods) {
        this.gods = gods;
    }

    public void setNumberRequest(int numberRequest) {
        this.numberRequest = numberRequest;
    }
}