package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;

public class BoardSceneController implements ViewGuiController {

    private View view;

    @Override
    public void setView(View view) {
        this.view = view;
    }
}
