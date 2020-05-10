package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Worker {

    private Color color;
    private Position position;
    private History history;
    private Set<MoveType> lockedMovements;

    public Worker(Position position) {
        this.position = position;
        this.history = new History();
        this.lockedMovements = new HashSet<>();
    }

    public Worker(Color color) {
        this.color = color;
        this.lockedMovements = new HashSet<>();
    }

    public void initPosition(Position position) {
        this.position = position;
        this.history = new History();
    }

    /**
     * Move the Worker to the given position.
     *
     * @param position a valid position to move
     */
    public void move(Position position) {
        updateMoveHistory(this.position, Game.getInstance().getSpaceLevel(this.position));
        this.position = position; // Worker is now in the new position
    }

    /**
     * Returns the adjacent positions where this worker can normally build.
     * Non-free spaces will be ignored @see it.polimi.ingsw.model.Space#isFree.
     *
     * @return a list of positions where this worker can build.
     */
    public List<Position> getPossibleBuilds() {
        Board board = Game.getInstance().getBoard();

        return board.getNeighbours(position).stream()
                .filter(pos -> board.getSpace(pos).isFree())
                .collect(Collectors.toList());
    }

    /**
     * Returns a {@code List} of adjacent and "level compatible" positions to the worker's position.
     * Locked movements are also filtered.
     *
     * @return a {@code List} of adjacent and "level compatible" positions to the worker's position.
     */
    public List<Position> getPossibleMoves() {
        Board board = Game.getInstance().getBoard();
        Space currentSpace = board.getSpace(position);

        List<Position> possibleMoves = board.getNeighbours(position);
        possibleMoves.removeAll(board.getNeighbourWorkers(position, false));

        return possibleMoves.stream()
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) <= currentSpace.getLevel())
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) >= -1)
                .filter(pos -> !lockedMovements.contains(board.getMoveTypeByLevel(position, pos)))
                // FIXME
                //  .filter(pos -> !lockedMovements.contains(board.isMovingBack(position, pos)))
                .collect(Collectors.toList());
    }

    /**
     * Returns the level of the worker in his current position.
     *
     * @return the level of the worker in his current position.
     */
    public int getLevel() {
        Board board = Game.getInstance().getBoard();
        return board.getSpace(position).getLevel();
    }

    /**
     * Returns {@code true} if the worker has moved up by one level, {@code false} otherwise.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @return {@code true} if the worker has moved up by one level, {@code false} otherwise.
     */
    public boolean hasMovedUp() {
        return !position.equals(history.getMovePosition()) &&
                getLevel() == history.getMoveLevel() + 1;
    }

    /**
     * Returns {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @return {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     */
    public boolean hasMovedDown() {
        return !position.equals(history.getMovePosition()) &&
                getLevel() < history.getMoveLevel();
    }

    /**
     * Returns {@code true} if the worker has moved flat, {@code false} otherwise.
     * A move is considered to be "flat" if the level has not changed.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @return {@code true} if the worker has moved flat, {@code false} otherwise.
     */
    public boolean hasMovedFlat() {
        return !position.equals(history.getMovePosition()) &&
                getLevel() == history.getMoveLevel();
    }

    /**
     * Update the worker move history.
     *
     * @param position worker's position in the previous turn
     * @param level    worker's level in the previous turn
     */
    private void updateMoveHistory(Position position, int level) {
        history.setMovePosition(position);
        history.setMoveLevel(level);
    }

    /**
     * Updates the worker build history. Call this method only to update the worker history.
     * The block must be built with board methods.
     *
     * @param position worker's build position in the previous turn
     */
    public void updateBuildHistory(Position position) {
        history.setBuildPosition(position);
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Returns a copy of {@code History} of this worker.
     *
     * @return a copy of {@code History} of this worker.
     */
    public History getHistory() {
        return new History(this.history);
    }

    /**
     * Checks if this worker has the argument moveType locked.
     * This worker cannot perform a move in the direction of the argument moveType.
     * Returns {@code true} if the MoveType argument is locked, {@code false} otherwise.
     *
     * @param moveType the MoveType to be checked.
     * @return {@code true} if the MoveType argument is locked, {@code false} otherwise.
     */
    public boolean checkLockedMovement(MoveType moveType) {
        return lockedMovements.stream()
                .anyMatch(lm -> lm.equals(moveType));
    }

    public void addLockedMovement(MoveType moveType) {
        lockedMovements.add(moveType);
    }

    public void removeLockedMovement(MoveType moveType) {
        lockedMovements.remove(moveType);
    }

    public void removeAllLockedMovements() {
        lockedMovements.clear();
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
