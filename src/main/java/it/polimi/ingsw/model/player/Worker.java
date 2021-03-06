package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents the single Worker object which contains the main info and methods.
 */
public class Worker implements Serializable {

    private static final long serialVersionUID = 773685750902018150L;

    private Color color;
    private Position position;
    private History history;
    private final Set<MoveType> lockedMovements;

    /**
     * Default constructor.
     *
     * @param position Initial Position of the Worker.
     */
    public Worker(Position position) {
        this.position = position;
        this.history = new History();
        this.lockedMovements = new HashSet<>();
    }

    /**
     * Constructor with color.
     *
     * @param color Color of the Worker.
     */
    public Worker(Color color) {
        this.color = color;
        this.lockedMovements = new HashSet<>();
    }

    /**
     * Set the initial Position of the Worker.
     *
     * @param position Position of the Worker.
     */
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
     * Non-free spaces will be ignored {@link Space#isFree}.
     * Set the boolean parameter to {@code true} to ignore the current worker position in the list
     * of the possible builds. This is needed to calculate the list of possible builds before
     * the actual move is done.
     *
     * @param checkPos              the starting position to check
     * @param ignoreCurrentPosition set to {@code true} to ignore the current position of the worker, {@code false} otherwise.
     * @return a list of positions where this worker can build.
     */
    public List<Position> getPossibleBuilds(Position checkPos, boolean ignoreCurrentPosition) {
        Board board = Game.getInstance().getBoard();

        return board.getNeighbours(checkPos).stream()
                .filter(pos -> (!ignoreCurrentPosition && pos.equals(this.position)) || board.getSpace(pos).isFree())
                .collect(Collectors.toList());
    }

    /**
     * Returns the adjacent positions where this worker can normally build.
     * Non-free spaces will be ignored {@link Space#isFree}.
     *
     * @return a list of positions where this worker can build.
     */
    public List<Position> getPossibleBuilds() {
        return getPossibleBuilds(this.position, true);
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

        // Remove all possible destination in which the worker couldn't build.
        possibleMoves.removeIf(pos -> getPossibleBuilds(pos, false).isEmpty());

        return possibleMoves.stream()
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) <= currentSpace.getLevel())
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) >= -1)
                .filter(pos -> board.getSpace(pos).isFree())
                .filter(pos -> !lockedMovements.contains(board.getMoveTypeByLevel(position, pos)))
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

    /**
     * Return the Position of the Worker.
     *
     * @return Position of the Worker.
     */
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

    /**
     * Filters a list of positions argument by removing all the positions
     * in conflict with the current locked movements applied to the worker.
     *
     * @param positions a list of positions to be filtered.
     * @return a list of filtered positions.
     */
    public List<Position> filterLockedMovementPositions(List<Position> positions) {
        Board board = Game.getInstance().getBoard();
        return positions.stream()
                .filter(pos -> !lockedMovements.contains(board.getMoveTypeByLevel(position, pos)))
                .collect(Collectors.toList());
    }

    /**
     * Adds a locked movement to the worker.
     * The worker will no more be able to move in that way.
     *
     * @param moveType the movement type to be locked.
     */
    public void addLockedMovement(MoveType moveType) {
        lockedMovements.add(moveType);
    }

    /**
     * Removes a locked movement applied to the worker.
     * The worker will be able again to move in that way.
     *
     * @param moveType the movement type to be unlocked.
     */
    public void removeLockedMovement(MoveType moveType) {
        lockedMovements.remove(moveType);
    }

    /**
     * Removes all locked movements applied to the worker.
     * The worker will be able again to move in every way normally possible.
     */
    public void removeAllLockedMovements() {
        lockedMovements.clear();
    }

    /**
     * Return the Color of the Worker.
     *
     * @return Color of the Worker.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the Color of the Worker.
     *
     * @param color Color of the Worker.
     */
    public void setColor(Color color) {
        this.color = color;
    }

}
