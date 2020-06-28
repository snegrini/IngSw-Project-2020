package it.polimi.ingsw.utils;

import it.polimi.ingsw.controller.GameController;

import java.io.Serializable;

public class Persistence implements Serializable {

    private static final long serialVersionUID = 6970123627732910952L;
    private final GameController gameController;

    public Persistence(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

}
