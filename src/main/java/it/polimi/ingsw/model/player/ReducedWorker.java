package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;

import java.io.Serializable;

/**
 * This class identifies a reduced version of the class {@link Worker}.
 * It is used inside the messages between client and server in order to
 * avoid sending unnecessary methods to the client.
 */
public class ReducedWorker implements Serializable {

    private static final long serialVersionUID = 6276055061235883140L;

    private final Color color;
    private final Position position;

    /**
     * Default constructor.
     *
     * @param worker the worker to produce a reduced version from.
     */
    public ReducedWorker(Worker worker) {
        this.color = worker.getColor();
        this.position = worker.getPosition();
    }

    /**
     * Return the Color of the Worker.
     *
     * @return Color of the Worker.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the Position of the Worker.
     *
     * @return Position of the Worker.
     */
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "ReducedWorker{" +
                "color=" + color +
                ", position=" + position +
                '}';
    }
}
