package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.gui.SceneController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class FirstPlayerSceneController extends ViewObservable implements GenericSceneController {

    private List<String> nicknames;
    private List<ReducedGod> gods;

    @FXML
    private Label player1Lbl;
    @FXML
    private Label player2Lbl;
    @FXML
    private Label player3Lbl;

    @FXML
    private ImageView player1godImg;
    @FXML
    private ImageView player2godImg;
    @FXML
    private ImageView player3godImg;

    @FXML
    private Group player1Group;
    @FXML
    private Group player2Group;
    @FXML
    private Group player3Group;

    @FXML
    private Button backToMenuBtn;

    public FirstPlayerSceneController() {
        nicknames = new ArrayList<>();
        gods = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        player3Group.setVisible(false);

        player1Lbl.setText(nicknames.get(0));
        Image img1 = new Image(getClass().getResourceAsStream("/images/gods/podium_" + gods.get(0).getName().toLowerCase() + ".png"));
        player1godImg.setImage(img1);

        player2Lbl.setText(nicknames.get(1));
        Image img2 = new Image(getClass().getResourceAsStream("/images/gods/podium_" + gods.get(1).getName().toLowerCase() + ".png"));
        player2godImg.setImage(img2);

        if (nicknames.size() == 3 && gods.size() == 3) {
            player3Group.setVisible(true);

            player3Lbl.setText(nicknames.get(2));
            Image img3 = new Image(getClass().getResourceAsStream("/images/gods/podium_" + gods.get(2).getName().toLowerCase() + ".png"));
            player3godImg.setImage(img3);
        }

        player1Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(event, nicknames.get(0)));
        player2Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(event, nicknames.get(1)));
        player3Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(event, nicknames.get(2)));

        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    private void onGroupClick(MouseEvent event, String nickname) {
        disableAllGroups();
        Platform.runLater(() -> notifyObserver(obs -> obs.onUpdateFirstPlayer(nickname)));
    }

    /**
     * Disable all the clickable groups in the scene.
     */
    private void disableAllGroups() {
        player1Group.setDisable(true);
        player2Group.setDisable(true);
        player3Group.setDisable(true);
    }

    private void onBackToMenuBtnClick(Event event) {
        // TODO disconnect
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }


    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    public void setGods(List<ReducedGod> gods) {
        this.gods = gods;
    }
}
