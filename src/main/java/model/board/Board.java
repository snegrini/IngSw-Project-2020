package model.board;

import model.Game;
import model.board.Position;
import model.board.Space;
import model.enumerations.Color;
import model.enumerations.MoveType;
import model.player.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
                spaces[i][j] = new Space();
            }
        }
    }

    /**
     * Returns the free positions on the board.
     *
     * @return the free positions on the board.
     */
    public List<Position> getFreePositions() {
        List<Position> positionList = new ArrayList<>();
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                if (spaces[i][j].isFree()) {
                    Position position = new Position(i, j);
                    positionList.add(position);
                }
            }
        }
        return positionList;
    }

    /**
     * Check if positionList refers only to free spaces.
     *
     * @param positionList positionList from client.
     * @return {@code true} if ALL the positions in the argument list are free, {@code false} otherwise.
     */
    public boolean arePositionsFree(List<Position> positionList) {
        boolean areFree = true;
        for (Position p : positionList) {
            if (!this.getSpace(p).isFree()) {
                areFree = false;
            }
        }
        return areFree;
    }

    /**
     * Returns the space at the given position.
     *
     * @param position position of a Space.
     * @return the {@code Space} at the given Position.
     */
    public Space getSpace(Position position) {
        return getSpace(position.getRow(), position.getColumn());
    }

    /**
     * Returns the space at the given coordinates (row, column).
     *
     * @param row the row of the Space.
     * @param col the column of the Space.
     * @return the {@code Space} at the given coordinates.
     */
    public Space getSpace(int row, int col) {
        return spaces[row][col];
    }

    /**
     * Return the next Space on the line passing between {@code orig} and {@code dest}.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the next Space on the line passing between {@code orig} and {@code dest}.
     */
    public Space getNextSpaceInLine(Position orig, Position dest) {
        int tempRow = orig.getRow() - dest.getRow();
        int tempCol = orig.getColumn() - dest.getColumn();

        return spaces[dest.getRow() - tempRow][dest.getColumn() - tempCol];
    }

    /**
     * Returns a list of positions that are adjacent to the position argument.
     *
     * @param position The position to look for the neighbours.
     * @return The list of spaces adjacent to this space.
     */
    public List<Position> getNeighbours(Position position) {
        List<Position> neighbours = new ArrayList<>();

        int targetRow = position.getRow();
        int targetCol = position.getColumn();

        for (int x = max(0, targetRow - 1); x <= min(targetRow + 1, MAX_ROWS - 1); x++) {
            for (int y = max(0, targetCol - 1); y <= min(targetCol + 1, MAX_COLUMNS - 1); y++) {
                if (x != targetRow || y != targetCol) {
                    neighbours.add(new Position(x, y));
                }
            }
        }
        return neighbours;
    }

    /**
     * Returns a list of positions that are adjacent to the position argument and are occupied by an opponent worker.
     *
     * @param position The position to look for the neighbours.
     * @return The list of spaces adjacent to this space.
     */
    public List<Position> getNeighbourWorkers(Position position) {
        Worker worker = getSpace(position).getWorker();
        Color color = worker.getColor();

        return getNeighbours(position).stream()
                .filter(pos -> getSpace(pos).getWorker() != null)
                .filter(pos -> !color.equals(getSpace(pos).getWorker().getColor()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the MoveType needed to perform the move from the first position argument to
     * the second position argument. Returns {@code null} if the arguments are not neighbours.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the MoveType needed to perform the move from the first position argument to
     * the second position argument. Returns {@code null} if the arguments are not neighbours.
     */
    public MoveType getMoveType(Position orig, Position dest) {
        // Check if the arguments are neighbours.
        if (!getNeighbours(orig).contains(dest)) {
            return null;
        }

        int lvlOrig = getSpace(orig).getLevel();
        int lvlDest = getSpace(dest).getLevel();

        if (!orig.equals(dest)) {
            if (lvlOrig < lvlDest) {
                return MoveType.UP;
            } else if (lvlOrig > lvlDest) {
                return MoveType.DOWN;
            } else {
                return MoveType.FLAT;
            }
        } else {
            return MoveType.NONE;
        }
    }

    /**
     * Resets all the spaces' levels in the board.
     */
    public void resetAllLevels() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                spaces[i][j].decreaseLevel(spaces[i][j].getLevel());
            }
        }
    }
}
