package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.ReducedWorker;

import java.io.Serializable;

public class ReducedSpace implements Serializable {
    private static final long serialVersionUID = 961249489188346446L;
    private final int level;
    private final boolean dome;
    private final ReducedWorker reducedWorker;

    public ReducedSpace(Space space) {
        this.level = space.getLevel();
        this.dome = space.hasDome();

        if (space.getWorker() == null) {
            this.reducedWorker = null;
        } else {
            this.reducedWorker = new ReducedWorker(space.getWorker());
        }
    }

    public int getLevel() {
        return level;
    }

    public boolean hasDome() {
        return dome;
    }

    public ReducedWorker getReducedWorker() {
        return reducedWorker;
    }
}
