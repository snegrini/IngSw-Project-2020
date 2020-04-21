package model.board;

import model.player.ReducedWorker;
import model.player.Worker;

public class ReducedSpace {
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
