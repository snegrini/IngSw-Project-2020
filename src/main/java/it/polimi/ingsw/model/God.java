package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.observer.Observer;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * This Class represents the God object.
 */
public class God implements Serializable {

    private static final long serialVersionUID = 92615339614352879L;

    private final String name;
    private final String caption;
    private final String description;
    private final List<Effect> effects;

    /**
     * Default constructor.
     * @param builder
     */
    private God(Builder builder) {
        this.name = builder.name;
        this.caption = builder.caption;
        this.description = builder.description;
        this.effects = builder.effects;
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

    /**
     * Returns the god {@code Effect} given the PhaseType, {@code null} otherwise.
     * Only the first occurrence is returned; a God should have only one Effect per PhaseType.
     *
     * @param phaseType the type of the effect to get.
     * @return the {@code Effect} of the god if it is found, {@code null} otherwise.
     */
    public Effect getEffectByType(PhaseType phaseType) {
        return effects.stream()
                .filter(effect -> phaseType.equals(effect.getPhaseType()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds an observer to all the effects of the god.
     * This is necessary in order to perform the effect.
     *
     * @param obs the observer to be added.
     */
    public void addObserverToAllEffects(Observer obs) {
        for (Effect effect : effects) {
            effect.addObserver(obs);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        God god = (God) o;
        return name.equals(god.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Builder class used to create new gods.
     */
    public static class Builder {
        private final String name;
        private String caption;
        private String description;
        private List<Effect> effects;

        /**
         * Default constructor.
         *
         * @param name the name of the god.
         */
        public Builder(String name) {
            this.name = name;
        }

        /**
         * Return a Builder with caption.
         * @param caption caption of the builder.
         * @return Builder with caption.
         */
        public Builder withCaption(String caption) {
            this.caption = caption;
            return this;
        }


        /**
         * Return a Builder with description.
         * @param description description of the builder.
         * @return Builder with description.
         */
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Return a Builder with effects.
         * @param effects effects of the builder.
         * @return Builder with effects.
         */
        public Builder withEffects(List<Effect> effects) {
            this.effects = effects;
            return this;
        }

        /**
         * Return the God builded.
         * @return God builded with Builder.
         */
        public God build() {
            return new God(this);
        }
    }
}
