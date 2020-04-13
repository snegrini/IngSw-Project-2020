package controller;

import model.Game;

public class TurnController {

    private Game gameInstance;
    private PhaseController phaseController;

    TurnController(Game gameInstance) {
        this.phaseController = new PhaseController(gameInstance.getPlayers());
    }
}
