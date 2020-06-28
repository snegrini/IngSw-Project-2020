package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.SceneController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class implements the scene which show information about a given god.
 */
public class GodInfoSceneController implements GenericSceneController {
    private final Stage stage;
    private String godName;

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
    }

    @FXML
    public void initialize() {
        okBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onOkBtnClick);
    }

    /**
     * Handle click on Ok button.
     * @param event the mouse click event.
     */
    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    /**
     * Set the god's name.
     * @param str name of the god.
     */
    public void setGodName(String str) {
        nameLbl.setText(str);
        godName = str;
    }

    /**
     * Set the god's caption.
     * @param str caption of the god.
     */
    public void setGodCaption(String str) {
        captionLbl.setText(str);
    }

    /**
     * Set the god's description.
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
     * Set the scene.
     * @param scene scene of the stage.
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * Set the god's image.
     */
    public void setGodImage() {
        Image img = new Image(getClass().getResourceAsStream("/images/gods/podium_" + godName.toLowerCase() + ".png"));
        godImg.setImage(img);
    }
}
