package model.effects;


import model.enumerations.EffectType;
import model.player.Player;
import model.player.Worker;

import java.util.List;
import java.util.Map;

/**
 * Abstract class used to implement the Decorator Pattern for the effects.
 */
public abstract class Effect {

    private EffectType effectType;
    protected Map<String, String> requirements;
    protected Map<String, String> parameters;

    /**
     * Applies the effect to the argument workers.
     *
     * @param targetWorkers the workers to whose apply the effect.
     */
    public abstract void apply(List<Worker> targetWorkers);

    /**
     * Checks the necessary conditions for the effect to be applied.
     *
     * @param worker worker to whose check the effect requirements.
     * @return {@code true} if the conditions are satisfied, {@code false} otherwise.
     */
    public abstract boolean require(Worker worker);

    public EffectType getEffectType() {
        return effectType;
    }

    /**
     * Sets the effect type only if it hasn't already been set.
     * The attribute {@code effectType} may not be modified again from decorators.
     *
     * @param effectType the type of the effect.
     */
    public void setEffectType(EffectType effectType) {
        if (null == this.effectType) {
            this.effectType = effectType;
        }
    }
}
