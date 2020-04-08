package model;

import model.effects.Effect;

import java.util.List;

public class God {
    private int id;
    private String name;
    private String caption;
    private String description;
    private List<Effect> effects;

    public static class Builder {
        private int id;
        private String name;
        private String caption;
        private String description;
        private List<Effect> effects;

        public Builder(int id){
            this.id = id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return  this;
        }

        public Builder withEffects(List <Effect> effects) {
            this.effects = effects;
            return this;
        }

        public God build(){
            return new God(this);
        }

    }

    private God(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.caption = builder.caption;
        this.description = builder.description;
        this.effects = builder.effects;
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
