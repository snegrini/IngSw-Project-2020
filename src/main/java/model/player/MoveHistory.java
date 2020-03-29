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
     * A worker has moved up if his player has moved it during his turn.
     *
     * @param currentPosition
     * @param currentLevel
     * @return {@code true} if the worker has moved up by one level, {@code false} otherwise.
     */
    public boolean hasMovedUp(Position currentPosition, int currentLevel) {
        return !currentPosition.equals(lastPosition) &&
                currentLevel == lastLevel + 1;
    }

    /**
     *
     * @param currentPosition
     * @param currentLevel
     * @return {@code true} if the worker has moved down by one or more levels, {@code false} otherwise.
     */
    public boolean hasMovedDown(Position currentPosition, int currentLevel) {
        return !currentPosition.equals(lastPosition) &&
                currentLevel < lastLevel;
    }

    /**
     *
     * @param currentPosition
     * @param currentLevel
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
