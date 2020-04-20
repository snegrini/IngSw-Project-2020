package controller;

import model.Game;
import model.player.Player;

public class TurnController {

    private Game gameInstance;
    private PhaseController phaseController;

    TurnController(Game gameInstance) {
        this.phaseController = new PhaseController(gameInstance.getPlayers());
    }

    public Player getActivePlayer() {
        return phaseController.getActivePlayer();
    }


}
