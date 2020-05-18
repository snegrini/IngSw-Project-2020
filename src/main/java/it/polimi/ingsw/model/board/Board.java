package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.History;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.observer.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Board extends Observable {

    public static final int MAX_ROWS = 5;
    public static final int MAX_COLUMNS = 5;

    private final Space[][] spaces;

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
     * Sets the workers on the board at the worker position. This method should be called only on game start.
     *
     * @param workers a list of workers.
     */
    public void initWorkers(List<Worker> workers) {
        for (Worker w : workers) {
            getSpace(w.getPosition()).setWorker(w);
        }
        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }

    /**
     * Returns a worker given the position argument.
     *
     * @param position the position of the worker.
     * @return the worker found, {@code null} otherwise.
     */
    public Worker getWorkerByPosition(Position position) {
        return getSpace(position).getWorker();
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
     * @return the next Space on the line passing between {@code orig} and {@code dest},
     * {@code null} if the next Space on the line is invalid.
     * {@link #getNextPositionInLine(Position, Position)}.
     */
    public Space getNextSpaceInLine(Position orig, Position dest) {
        Position position = getNextPositionInLine(orig, dest);
        if (position != null) {
            return getSpace(position);
        } else {
            return null;
        }
    }

    /**
     * Return the next position on the line passing between {@code orig} and {@code dest}.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the next position on the line passing between {@code orig} and {@code dest}.
     */
    public Position getNextPositionInLine(Position orig, Position dest) {
        int newRow = (2 * dest.getRow()) - orig.getRow();
        int newCol = (2 * dest.getColumn()) - orig.getColumn();

        return isPositionOnBoard(newRow, newCol) ? new Position(newRow, newCol) : null;
    }

    /**
     * Checks if the given position is on the board.
     *
     * @param position the position to be checked.
     * @return {@code true} if the position is on the board, {@code false} otherwise.
     * {@link #isPositionOnBoard(int, int)}.
     */
    public boolean isPositionOnBoard(Position position) {
        return isPositionOnBoard(position.getRow(), position.getColumn());
    }

    /**
     * Checks if the given position is on the board.
     *
     * @param row the row the position to be checked.
     * @param col the column of the position to be checked.
     * @return {@code true} if the position is on the board, {@code false} otherwise.
     */
    public boolean isPositionOnBoard(int row, int col) {
        return row >= 0 && col >= 0 && row < MAX_ROWS && col < MAX_COLUMNS;
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
     * Returns a list of positions that are adjacent to the position argument and are occupied by a worker.
     *
     * @param position The position to look for the neighbours.
     * @param oppOnly  If set to {@code true} only opponent workers are checked.
     * @return The list of spaces adjacent to this space.
     */
    public List<Position> getNeighbourWorkers(Position position, boolean oppOnly) {
        Worker worker = getSpace(position).getWorker();
        Color color = (worker != null) ? worker.getColor() : null;

        return getNeighbours(position).stream()
                .filter(pos -> getSpace(pos).getWorker() != null)
                .filter(pos -> !oppOnly || !color.equals(getSpace(pos).getWorker().getColor()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the MoveType needed to perform the move from the first position argument to
     * the second position argument. Comparison is done by checking the current levels
     * of the spaces.
     * Returns {@code null} if the arguments are not neighbours.
     *
     * @param orig the starting position.
     * @param dest the destination position.
     * @return the MoveType needed to perform the move from the first position argument to
     * the second position argument. Returns {@code null} if the arguments are not neighbours or if
     * the origin position is the same as the destination.
     */
    public MoveType getMoveTypeByLevel(Position orig, Position dest) {
        // Check if the arguments are neighbours.
        if (!getNeighbours(orig).contains(dest) || orig.equals(dest)) {
            return null;
        }

        int lvlOrig = getSpace(orig).getLevel();
        int lvlDest = getSpace(dest).getLevel();

        if (lvlOrig < lvlDest) {
            return MoveType.UP;
        } else if (lvlOrig > lvlDest) {
            return MoveType.DOWN;
        } else {
            return MoveType.FLAT;
        }
    }

    /**
     * Returns {@code true} if the worker is moving back into his last position
     *
     * @param worker the worker to check the move.
     * @param dest   the destination position.
     * @return the MoveType needed to perform the move from the first position argument to
     * the second position argument. Returns {@code null} if the arguments are not neighbours.
     * @see History , {@code false} otherwise.
     */
    public boolean isMovingBack(Worker worker, Position dest) {
        Position orig = worker.getPosition();

        // Check if the arguments are neighbours.
        if (!getNeighbours(orig).contains(dest) || orig.equals(dest)) {
            return false;
        }

        Position lastPosition = worker.getHistory().getMovePosition();

        return dest.equals(lastPosition);
    }

    /**
     * Resets all the spaces' levels and domes in the board.
     */
    public void resetAllLevels() {
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                spaces[i][j].decreaseLevel(spaces[i][j].getLevel());
                spaces[i][j].setDome(false);
            }
        }
    }

    /**
     * Returns a matrix of ReducedSpace wich is immutable object.
     *
     * @return a board of reduced spaces
     */
    public ReducedSpace[][] getReducedSpaceBoard() {
        ReducedSpace[][] reducedBoard = new ReducedSpace[MAX_ROWS][MAX_COLUMNS];
        for (int i = 0; i < MAX_ROWS; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                reducedBoard[i][j] = new ReducedSpace(spaces[i][j]);
            }
        }
        return reducedBoard;
    }

    /**
     * Moves a worker to the given {@code Position}.
     * Finally, a notification to the views is sent.
     *
     * @param worker the worker to be moved.
     * @param dest   the destination of the move.
     */
    public void moveWorker(Worker worker, Position dest) {
        getSpace(worker.getPosition()).setWorker(null);
        worker.move(dest);
        getSpace(dest).setWorker(worker);
        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }

    /**
     * Swaps the position of two workers in the board.
     * Finally, a notification to the views is sent.
     *
     * @param worker1 the first worker.
     * @param worker2 the second worker.
     */
    public void swapWorkers(Worker worker1, Worker worker2) {
        Space space1 = getSpace(worker1.getPosition());
        Space space2 = getSpace(worker2.getPosition());

        Position oldPos = worker1.getPosition();
        worker1.move(worker2.getPosition());
        worker2.move(oldPos);

        space1.setWorker(worker2);
        space2.setWorker(worker1);
        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }


    /**
     * Builds a single block over the {@code Space} at the given position.
     *
     * @param worker the worker who builds.
     * @param dest   the space position to build onto.
     */
    public void buildBlock(Worker worker, Position dest) {
        Space space = getSpace(dest);
        if (space.getLevel() == 3) {
            space.setDome(true);
        } else {
            space.increaseLevel(1);
        }
        worker.updateBuildHistory(dest);
        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }

    /**
     * Builds a dome over the {@code Space} at the given position.
     * Level checks are bypassed.
     *
     * @param worker the worker who builds.
     * @param dest   the space position to build onto.
     */
    public void buildDomeForced(Worker worker, Position dest) {
        Space space = getSpace(dest);
        space.setDome(true);
        worker.updateBuildHistory(dest);
        notifyObserver(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, getReducedSpaceBoard()));
    }
}
