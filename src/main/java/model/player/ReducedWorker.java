package model.player;

import model.board.Position;
import model.enumerations.Color;

import java.io.Serializable;

public class ReducedWorker implements Serializable {
    private static final long serialVersionUID = 6276055061235883140L;
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
