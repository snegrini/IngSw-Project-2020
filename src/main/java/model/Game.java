package model;

public class Game {

    public static Game instance;
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
