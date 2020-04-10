package controller;

import model.Game;

public class TurnController {

    private Game gameInstance;
    private ServerController serverController;
    private PhaseController phaseController;

    TurnController(Game gameInstance, ServerController serverController) {
        this.gameInstance = gameInstance;
        this.serverController = serverController;
    }

    void initPhaseController() {
        this.phaseController = new PhaseController(gameInstance.getPlayers());
    }

    void initPhaseController(PhaseController phaseController) {
        this.phaseController = new TurnController(phaseController);
    }

}
