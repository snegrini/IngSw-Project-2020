package model;

import java.io.Serializable;
import java.util.Objects;

public class ReducedGod implements Serializable {
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
