package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.observer.Observer;

import java.util.List;
import java.util.Objects;

public class God {
    private final String name;
    private final String caption;
    private final String description;
    private final List<Effect> effects;

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

    public static class Builder {
        private final String name;
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

        public Builder withEffects(List<Effect> effects) {
            this.effects = effects;
            return this;
        }

        public God build() {
            return new God(this);
        }
    }
}
