package controller;

import model.Game;
import model.player.Player;

import java.util.List;

public class PhaseController {
    private Player activePlayer;
    private List<Player> players;

    public PhaseController(List<Player> players) {
        this.players = players;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
}
