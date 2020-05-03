package it.polimi.ingsw.PSP016.model.player;

import it.polimi.ingsw.PSP016.model.board.Position;

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
