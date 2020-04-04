package model.board;

import model.board.Position;
import model.player.Worker;

public class Space {

    private int level;
    private boolean dome;
    private Worker worker;

    public Space() {
        this.level = 0;
        this.dome = false;
    }

    /*public boolean isBorderline() { }*/

    public boolean isFree() {
        return (worker == null) && !dome;
    }

    public int getLevel() {
        return level;
    }

    public boolean increaseLevel(int value) {
        if (value < 0) return false;

        level += value;
        return true;
    }

    public boolean decreaseLevel(int value) {
        // Meglio eccezione di return, per esempio ExpectedNegativeValue ?
        if (value > 0) return false;

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
}
