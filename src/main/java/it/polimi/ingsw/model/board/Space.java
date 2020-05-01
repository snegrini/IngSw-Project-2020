package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Worker;

public class Space implements Comparable<Space> {
    private int level;
    private boolean dome;
    private Worker worker;

    public final static int MIN_LEVEL = 0;
    public final static int MAX_LEVEL = 3;

    public Space() {
        this.level = 0;
        this.dome = false;
    }

    public boolean isFree() {
        return (worker == null) && !dome;
    }

    public int getLevel() {
        return level;
    }

    public boolean increaseLevel(int value) {
        if (value < 0 || level + value > MAX_LEVEL) {
            return false;
        }
        level += value;
        return true;
    }

    public boolean decreaseLevel(int value) {
        if (value < 0 || level - value < MIN_LEVEL) {
            return false;
        }
        level -= value;
        return true;
    }

    public boolean hasDome() {
        return dome;
    }

    public void setDome(boolean dome) {
        this.dome = dome;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * Compares this space level to the specified space level. The result is
     * the value {@code 0} if the space argument level is equal to
     * this level; a value less than {@code 0} if this level
     * is less than the level argument; and a value greater than {@code 0}
     * if this level is greater than the level of the space argument.
     * The values returned are the difference between the levels considered.
     *
     * @param anotherSpace The space to compare this space level against.
     * @return the value {@code 0} if the space argument level is equal to
     * this level; a value less than {@code 0} if this level
     * is less than the level argument; and a value greater than {@code 0}
     * if this level is greater than the level of the space argument.
     */
    @Override
    public int compareTo(Space anotherSpace) {
        return this.level - anotherSpace.level;
    }
}
