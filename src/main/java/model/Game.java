package model;

import model.board.Board;
import model.player.Player;
import observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Game extends Observable {

    private static Game instance;

    public static final int MAX_PLAYERS = 3;
    private int chosenPlayersNumber;

    private Board board;
    private List<Player> players;

    private Game() {
        this.board = new Board();
        this.players = new ArrayList<>();
    }

    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Returns a player given his {@code nickname}.
     * Only the first occurrence is returned because
     * the player nickname is considered to be unique.
     * If no player is found {@code null} is returned.
     *
     * @param nickname the nickname of the player to be found.
     * @return Returns the player given his {@code nickname}, {@code null} otherwise.
     */
    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add to the game.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Number of current players added in the game.
     *
     * @return the number of players.
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }

    /**
     * Sets the max number of players chosen by the first player joining the game.
     *
     * @param chosenMaxPlayers the max players number. Value can be 0 < x < {@code MAX_PLAYERS}.
     */
    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers < MAX_PLAYERS) {
            this.chosenPlayersNumber = chosenMaxPlayers;
            return true;
        }
        return false;
    }

    public int getChosenPlayersNumber() {
        return chosenPlayersNumber;
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return players;
    }


    public boolean isPlayerInList(String nickname) {
        for (Player p : players) {
            if (p.getNickname().equals(nickname))
                return true;
        }
        return false;
    }
}
