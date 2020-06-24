package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.ReducedWorker;

import java.io.Serializable;

/**
 * This class identifies a reduced version of the class {@link Space}.
 * It is used inside the messages between client and server in order to
 * avoid sending unnecessary methods to the client.
 */
public class ReducedSpace implements Serializable {
    private static final long serialVersionUID = 961249489188346446L;
    private final int level;
    private final boolean dome;
    private final ReducedWorker reducedWorker;

    /**
     * Constructor of Reduced Space.
     * @param space space to reduce.
     */
    public ReducedSpace(Space space) {
        this.level = space.getLevel();
        this.dome = space.hasDome();

        if (space.getWorker() == null) {
            this.reducedWorker = null;
        } else {
            this.reducedWorker = new ReducedWorker(space.getWorker());
        }
    }

    /**
     * Returns the level of the reduced space.
     * @return level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the presence of a dome on a space.
     * @return {code @true} if contains a Dome {code @false} otherwise.
     */
    public boolean hasDome() {
        return dome;
    }

    /**
     * Return the occupant reduced worker on the space.
     * @return Reduced Worker.
     */
    public ReducedWorker getReducedWorker() {
        return reducedWorker;
    }
}
