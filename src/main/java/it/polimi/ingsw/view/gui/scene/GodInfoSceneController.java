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

    private void onOkBtnClick(MouseEvent event) {
        stage.close();
    }

    public void setGodName(String str) {
        nameLbl.setText(str);
        godName = str;
    }

    public void setGodCaption(String str) {
        captionLbl.setText(str);
    }

    public void setGodDescription(String str) {
        descriptionLbl.setText(str);
    }

    public void displayGodInfo() {
        stage.showAndWait();
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public void setGodImage() {
        Image img = new Image(getClass().getResourceAsStream("/images/gods/podium_" + godName.toLowerCase() + ".png"));
        godImg.setImage(img);
    }
}
