package controller;

import model.Game;
import model.player.Player;
import network.message.Move;
import view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnController {

    private Game game;
    private List<String> nicknameQueue;
    private String activePlayer;

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
        } else  {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
    }


    private void move(Move receivedMessage, VirtualView virtualView) {
        // TODO
        // check if destination is free
        // check for some lock from gods
        // move player
        // check win condition
    }

}
