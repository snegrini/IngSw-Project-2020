package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class identifies a reduced version of the class {@link God}.
 * It is used inside the messages between client and server in order to
 * avoid sending unnecessary methods to the client.
 */
public class ReducedGod implements Serializable {

    private static final long serialVersionUID = -2089913761654565866L;

    private final String name;
    private final String caption;
    private final String description;

    /**
     * Default constructor.
     * @param god God to reduce.
     */
    public ReducedGod(God god) {
        this.name = god.getName();
        this.caption = god.getCaption();
        this.description = god.getDescription();
    }

    /**
     * Return the name of the God.
     * @return name of the God.
     */
    public String getName() {
        return name;
    }


    /**
     * Return the caption of the God.
     * @return caption of the God.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Return the description of the God.
     * @return description of the God.
     */
    public String getDescription() {
        return description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReducedGod that = (ReducedGod) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ReducedGod{" +
                "name=" + name +
                '}';
    }
}
