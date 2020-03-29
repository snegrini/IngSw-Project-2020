package model.board;

import model.board.Position;
import model.player.Worker;

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
        return position.getRow() == 0 || position.getRow() == (Board.MAX_ROWS - 1)
                || position.getColumn() == 0 || position.getColumn() == (Board.MAX_COLUMNS - 1);
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
