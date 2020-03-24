package model;

public class Board {

    private static final int MAX_ROWS = 5;
    private static final int MAX_COLUMNS = 5;

    private Space[][] spaces;

    public Board() {
        this.spaces = new Space[MAX_ROWS][MAX_COLUMNS];
        initSpaces();

    }

    /**
     * Initializes every space in the board with the correct position.
     */
    private void initSpaces() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                spaces[i][j] = new Space(new Position(i, j));
            }
        }
    }
}
