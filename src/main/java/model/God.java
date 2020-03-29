package model;

import model.effects.Effect;

import java.util.List;

public class  God {
    private int id;
    private String name;
    private String caption;
    private String description;
    private List<Effect> effects;

    public God(int id, String name, String caption, String description, List<Effect> effects) {
        this.id = id;
        this.name = name;
        this.caption = caption;
        this.description = description;
        this.effects = effects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
}
