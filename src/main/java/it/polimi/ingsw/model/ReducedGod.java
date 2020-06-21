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

    public ReducedGod(God god) {
        this.name = god.getName();
        this.caption = god.getCaption();
        this.description = god.getDescription();
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

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
}
