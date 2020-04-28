package model;

import java.io.Serializable;

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
}
