package it.polimi.ingsw.PSP016.model;

import it.polimi.ingsw.PSP016.model.effects.Effect;
import it.polimi.ingsw.PSP016.model.enumerations.EffectType;

import java.util.List;

public class God {
    private String name;
    private String caption;
    private String description;
    private List<Effect> effects;

    private God(Builder builder) {
        this.name = builder.name;
        this.caption = builder.caption;
        this.description = builder.description;
        this.effects = builder.effects;
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

    /**
     * Returns the god {@code Effect} given the EffectType, {@code null} otherwise.
     * Only the first occurrence is returned; a God should have only one Effect per EffectType.
     *
     * @param effectType the type of the effect to get.
     * @return the {@code Effect} of the god if it is found, {@code null} otherwise.
     */
    public Effect getEffectByType(EffectType effectType) {
        return effects.stream()
                .filter(effect -> effectType.equals(effect.getEffectType()))
                .findFirst()
                .orElse(null);
    }

    public static class Builder {
        private String name;
        private String caption;
        private String description;
        private List<Effect> effects;

        public Builder(String name) {
            this.name = name;
        }

        public Builder withCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
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
