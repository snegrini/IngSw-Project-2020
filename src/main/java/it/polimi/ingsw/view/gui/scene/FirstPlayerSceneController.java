package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.gui.SceneController;
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

import static it.polimi.ingsw.view.gui.SceneController.GOD_IMAGE_PREFIX;

/**
 * This class implements the controller of the scene where the first player choose the first user.
 */
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

    /**
     * Default constructor.
     */
    public FirstPlayerSceneController() {
        nicknames = new ArrayList<>();
        gods = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        player3Group.setVisible(false);

        player1Lbl.setText(nicknames.get(0));
        Image img1 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(0).getName().toLowerCase() + ".png"));
        player1godImg.setImage(img1);

        player2Lbl.setText(nicknames.get(1));
        Image img2 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(1).getName().toLowerCase() + ".png"));
        player2godImg.setImage(img2);

        if (nicknames.size() == 3 && gods.size() == 3) {
            player3Group.setVisible(true);

            player3Lbl.setText(nicknames.get(2));
            Image img3 = new Image(getClass().getResourceAsStream(GOD_IMAGE_PREFIX + gods.get(2).getName().toLowerCase() + ".png"));
            player3godImg.setImage(img3);
        }

        player1Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(nicknames.get(0)));
        player2Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(nicknames.get(1)));
        player3Group.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> onGroupClick(nicknames.get(2)));

        backToMenuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onBackToMenuBtnClick);
    }

    /**
     * Handle the click on a player's group.
     */
    private void onGroupClick(String nickname) {
        disableAllGroups();
        new Thread(() -> notifyObserver(obs -> obs.onUpdateFirstPlayer(nickname))).start();
    }

    /**
     * Disable all the clickable groups in the scene.
     */
    private void disableAllGroups() {
        player1Group.setDisable(true);
        player2Group.setDisable(true);
        player3Group.setDisable(true);
    }

    /**
     * Handle click on back to menu button.
     *
     * @param event the mouse click event.
     */
    private void onBackToMenuBtnClick(Event event) {
        new Thread(() -> notifyObserver(ViewObserver::onDisconnection)).start();
        SceneController.changeRootPane(observers, event, "menu_scene.fxml");
    }


    /**
     * Set nicknames of connected players.
     *
     * @param nicknames nicknames of the players.
     */
    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    /**
     * Set gods of all connected players.
     *
     * @param gods gods of the players.
     */
    public void setGods(List<ReducedGod> gods) {
        this.gods = gods;
    }
}
