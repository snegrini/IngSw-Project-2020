package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.View;

/**
 * Generic interface used to define all the scene controllers with a generic type.
 */
public interface ViewGuiController {

    /**
     * Sets a view to the controller.
     *
     * @param view the current gui view.
     */
    void setView(View view);
}
