package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.MessageType;

public class Space implements Comparable<Space> {
    private int level;
    private boolean dome;
    private Worker worker;

    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 3;

    public Space() {
        this.level = 0;
        this.dome = false;
    }

    /**
     * Checks if the current space is occupied by a worker or if it has a dome.
     *
     * @return {@code true} if the current space is not occupied by a worker and it has not a dome,
     * {@code false} otherwise.
     */
    public boolean isFree() {
        return (worker == null) && !dome;
    }

    /**
     * Returns the current level of the space.
     *
     * @return the current level of the space.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increases the level of the space by the specified argument.
     * Returns {@code true} if the argument is greater than 0 and not exceeds {@code MAX_LEVEL}
     * and the increase is performed, {@code false} otherwise.
     *
     * @param value the number of levels.
     * @return {@code true} if the argument is greater than 0 and not exceeds {@code MAX_LEVEL}
     * and the increase is performed, {@code false} otherwise.
     */
    public boolean increaseLevel(int value) {
        if (value < 0 || level + value > MAX_LEVEL) {
            return false;
        }
        level += value;
        return true;
    }

    /**
     * Decreases the level of the space by the specified argument.
     * Returns {@code true} if the argument is greater than 0 and not exceeds {@code MAX_LEVEL}
     * and the increase is performed, {@code false} otherwise.
     *
     * @param value the number of levels.
     * @return {@code true} if the argument is greater than 0 and not exceeds {@code MAX_LEVEL}
     * and the decrease is performed, {@code false} otherwise.
     */
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

    public void removeWorker() {
        worker = null;
    }
}
