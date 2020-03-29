package model.board;

import model.board.Position;
import model.board.Space;

public class Board {

    public static final int MAX_ROWS = 5;
    public static final int MAX_COLUMNS = 5;

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

    /**
     * Returns the space at a given position.
     *
     * @param position position of a Square.
     * @return the Space at the given Position.
     */
    public Space getSpace(Position position) {
        return spaces[position.getRow()][position.getColumn()];
    }
}
