package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

public class BoardSceneController implements ViewGuiController {

    private View view;

    @FXML
    public void initialize() {

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }
}
