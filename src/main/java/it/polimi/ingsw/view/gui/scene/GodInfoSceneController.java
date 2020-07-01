package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class implements the scene which shows information about a given god.
 */
public class GodInfoSceneController implements GenericSceneController {
    private final Stage stage;
    private String godName;

    private double xOffset;
    private double yOffset;

    @FXML
    private StackPane rootPane;
    @FXML
    private Label nameLbl;
    @FXML
    private Label captionLbl;
    @FXML
    private Label descriptionLbl;
    @FXML
    private Button okBtn;
    @FXML
    private ImageView godImg;

    /**
     * Default constructor.
     */
    public GodInfoSceneController() {
        stage = new Stage();
        stage.initOwner(SceneController.getActiveScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        xOffset = 0;
        yOffset = 0;
    }

    @FXML
    public void initialize() {
        rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::onRootPaneMousePressed);
        rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onRootPaneMouseDragged);
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    /**
     * Handles the mouse pressed event preparing the coordinates for dragging the window.
     *
     * @param event the mouse pressed event.
     */
    private void onRootPaneMousePressed(MouseEvent event) {
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    /**
     * Handles the mouse dragged event by moving the window around the screen.
     *
     * @param event the mouse dragged event.
     */
    private void onRootPaneMouseDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * Handles click on ok button.
     *
     * @param event the mouse click event.
     */
    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    /**
     * Sets the god's name.
     * @param str name of the god.
     */
    public void setGodName(String str) {
        nameLbl.setText(str);
        godName = str;
    }

    /**
     * Sets the god's caption.
     * @param str caption of the god.
     */
    public void setGodCaption(String str) {
        captionLbl.setText(str);
    }

    /**
     * Sets the god's description.
     * @param str description of the god.
     */
    public void setGodDescription(String str) {
        descriptionLbl.setText(str);
    }

    /**
     * Display the god's info.
     */
    public void displayGodInfo() {
        stage.showAndWait();
    }

    /**
     * Sets the scene.
     *
     * @param scene scene of the stage.
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * Sets the god's image.
     */
    public void setGodImage() {
        Image img = new Image(getClass().getResourceAsStream("/images/gods/podium_" + godName.toLowerCase() + ".png"));
        godImg.setImage(img);
    }
}
