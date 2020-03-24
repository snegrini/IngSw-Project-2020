package model;

public class Space {

    private int level;
    private boolean dome;
    private final Position position;
    private Worker worker;

    public Space(Position position) {
        this.level = 0;
        this.dome = false;
        this.position = position;
    }

    public boolean isFree() {
        return worker == null;
    }

    public boolean isBuildable() {
        return isFree() && !dome;
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

    public Position getPosition() {
        return position;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
