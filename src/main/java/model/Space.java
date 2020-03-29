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

    public boolean isBorderLine() {
        return (position.getRow() - 1 < 0 || position.getRow() + 1 > 5
                || position.getColumn() - 1 < 0 || position.getColumn() + 1 > 5);
    }

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
