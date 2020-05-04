package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Worker {

    private Color color;
    private Position position;
    private MoveHistory moveHistory;
    private Set<MoveType> lockedMovements;

    public Worker(Position position) {
        this.position = position;
        this.moveHistory = new MoveHistory(position, 0);
        this.lockedMovements = new HashSet<>();
    }

    public Worker(Color color) {
        this.color = color;
        this.lockedMovements = new HashSet<>();
    }

    public void initPosition(Position position) {
        this.position = position;
        this.moveHistory = new MoveHistory(position, 0);
    }

    /**
     * Builds a single block over the {@code Space} at the given position.
     *
     * @param space the space to build onto.
     */
    public void build(Space space) {
        space.increaseLevel(1);
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
        return !position.equals(moveHistory.getLastPosition()) &&
                getLevel() == moveHistory.getLastLevel() + 1;
    }

    /**
     * Returns {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @return {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     */
    public boolean hasMovedDown() {
        return !position.equals(moveHistory.getLastPosition()) &&
                getLevel() < moveHistory.getLastLevel();
    }

    /**
     * Returns {@code true} if the worker has moved flat, {@code false} otherwise.
     * A move is considered to be "flat" if the level has not changed.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @return {@code true} if the worker has moved flat, {@code false} otherwise.
     */
    public boolean hasMovedFlat() {
        return !position.equals(moveHistory.getLastPosition()) &&
                getLevel() == moveHistory.getLastLevel();
    }

    /**
     * Update the worker move history.
     *
     * @param position worker's position in the previous turn
     * @param level    worker's level in the previous turn
     */
    private void updateMoveHistory(Position position, int level) {
        moveHistory.setLastPosition(position);
        moveHistory.setLastLevel(level);
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Returns a copy of {@code MoveHistory} of this worker.
     *
     * @return a copy of {@code MoveHistory} of this worker.
     */
    public MoveHistory getMoveHistory() {
        return new MoveHistory(this.moveHistory);
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
