package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    private Game game;
    private List<String> nicknameQueue;
    private String activePlayer;
    private Worker activeWorker;

    private EffectType phaseType;

    public TurnController() {
        this.game = Game.getInstance();
        nicknameQueue = new ArrayList<>(game.getPlayersNicknames());

        activePlayer = nicknameQueue.get(0); // TODO set first active player
    }

    /**
     * @return nickname of the active player
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     *  Set next active player.
     */
    public void next() {

        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
        phaseType = EffectType.YOUR_MOVE;
    }

    public void setActiveWorker(Worker worker) {
        this.activeWorker = worker;
    }

    public Worker getActiveWorker() {
        return activeWorker;
    }


    public void setPhaseType(EffectType turnPhaseType) {
        this.phaseType = turnPhaseType;
    }

    public EffectType getPhaseType() {
        return phaseType;
    }
}
