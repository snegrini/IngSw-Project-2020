package it.polimi.ingsw.persistence;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.TurnController;

import java.io.Serializable;

public class Persistence implements Serializable {

    private static final long serialVersionUID = -8111323357904853198L;
    private GameController gameController;

    public Persistence(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

}
