package controller;

import model.Game;
import model.player.Worker;
import network.message.PositionMessage;
import view.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    private Game game;
    private List<String> nicknameQueue;
    private String activePlayer;
    private Worker activeWorker;

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
    }

    public void setActiveWorker(Worker worker) {
        this.activeWorker = worker;
    }

    public Worker getActiveWorker() {
        return activeWorker;
    }
}
