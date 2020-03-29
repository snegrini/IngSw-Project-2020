package model;

import model.board.Board;

public class Game {

    private static Game instance;
    private Board board;

    private Game() {}

    /**
     *
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    public Board getBoard() {
        return board;
    }
}
