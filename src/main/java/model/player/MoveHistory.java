package model.player;

import model.board.Position;

/**
 * This class keeps the history of position and level of the worker.
 * Methods are available to establish the last move executed by the player.
 * A note about this class:
 * "Forced" is not "moved". Some God Effects may cause Workers to be "forced" into another space.
 * A Worker that is forced, is not considered to have moved.
 *
 */
public class MoveHistory {

    private Position lastPosition;
    private int lastLevel;

    public MoveHistory(Position lastPosition, int lastLevel) {
        this.lastPosition = lastPosition;
        this.lastLevel = lastLevel;
    }

    /**
     * Copy constructor.
     *
     * @param moveHistory the {@code MoveHistory} object to be copied.
     */
    public MoveHistory(MoveHistory moveHistory) {
        this.lastPosition = new Position(moveHistory.lastPosition);
        this.lastLevel = moveHistory.lastLevel;
    }

    /**
     * Returns {@code true} if the worker has moved up by one level, {@code false} otherwise.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @param currentPosition the current position of the worker.
     * @param currentLevel    the current level of the worker.
     * @return {@code true} if the worker has moved up by one level, {@code false} otherwise.
     */
    public boolean hasMovedUp(Position currentPosition, int currentLevel) {
        return !currentPosition.equals(lastPosition) &&
                currentLevel == lastLevel + 1;
    }

    /**
     * Returns {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @param currentPosition the current position of the worker.
     * @param currentLevel    the current level of the worker.
     * @return {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     */
    public boolean hasMovedDown(Position currentPosition, int currentLevel) {
        return !currentPosition.equals(lastPosition) &&
                currentLevel < lastLevel;
    }

    /**
     * Returns {@code true} if the worker has moved flat, {@code false} otherwise.
     * A move is considered to be "flat" if the level has not changed.
     * This methods compares the last saved position of the worker with the argument one.
     *
     * @param currentPosition the current position of the worker.
     * @param currentLevel the current level of the worker.
     * @return {@code true} if the worker has moved flat, {@code false} otherwise.
     */
    public boolean hasMovedFlat(Position currentPosition, int currentLevel) {
        return !currentPosition.equals(lastPosition) &&
                currentLevel == lastLevel;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }
}
