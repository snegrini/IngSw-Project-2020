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

    public void setLevel(int level) {
        this.level = level;
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
