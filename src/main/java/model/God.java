package model;

import model.effects.Effect;

import java.util.List;

public class God {
    private int id;
    private String name;
    private String caption;
    private String description;
    private List<Effect> effects;

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

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public List<Effect> getEffects() {
        return effects;
    }

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
}
