package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Position;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps the history of the last move and the last build operation
 * committed by the worker.
 * Methods are available to establish the last move and the last build executed by the player.
 * A note about this class:
 * "Forced" is not "moved". Some God Effects may cause Workers to be "forced" into another space.
 * A Worker that is forced, is not considered to have moved.
 */
public class History implements Serializable {

    private static final long serialVersionUID = 5092332759009034420L;

    private final Map<String, Position> lastPositions;
    private final Map<String, Integer> lastLevels;

    private static final String MOVE = "MOVE";
    private static final String BUILD = "BUILD";

    /**
     * Default constructor.
     */
    public History() {
        this.lastPositions = new HashMap<>();
        this.lastLevels = new HashMap<>();
    }

    /**
     * Copy constructor.
     *
     * @param history the {@code History} object to be copied.
     */
    public History(History history) {
        this.lastPositions = new HashMap<>(history.lastPositions);
        this.lastLevels = new HashMap<>(history.lastLevels);
    }

    /**
     * Return the last position of the move.
     *
     * @return last position.
     */
    public Position getMovePosition() {
        return lastPositions.get(MOVE);
    }

    /**
     * Set the last position of the move.
     *
     * @param lastPosition last position.
     */
    public void setMovePosition(Position lastPosition) {
        this.lastPositions.put(MOVE, lastPosition);
    }

    /**
     * Return the previous level of the worker.
     *
     * @return last level.
     */
    public int getMoveLevel() {
        return lastLevels.get(MOVE);
    }

    /**
     * Set the previous level of the worker.
     *
     * @param lastLevel last level.
     */
    public void setMoveLevel(int lastLevel) {
        this.lastLevels.put(MOVE, lastLevel);
    }

    /**
     * Returns the last position of the build.
     *
     * @return last position.
     */
    public Position getBuildPosition() {
        return lastPositions.get(BUILD);
    }

    /**
     * Sets the last position of the build.
     *
     * @param lastPosition the last position of the build to be set.
     */
    public void setBuildPosition(Position lastPosition) {
        this.lastPositions.put(BUILD, lastPosition);
    }
}
