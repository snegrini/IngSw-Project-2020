package model.player;

import model.board.Position;
import model.enumerations.Color;

public class ReducedWorker {
    private final Color color;
    private final Position position;

    public ReducedWorker(Worker worker) {
        this.color = worker.getColor();
        this.position = worker.getPosition();
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }
}
