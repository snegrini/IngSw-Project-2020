package model.effects;


import model.enumerations.EffectType;
import model.player.Player;
import model.player.Worker;

import java.util.List;

/**
 * Abstract class used to implement the Decorator Pattern for the effects.
 */
public abstract class Effect {

    protected EffectType effectType;

    /**
     *  Applies the effect.
     */
    public abstract void apply(List<Worker> targetWorkers);

    /**
     * Checks the necessary conditions for the effect to be applied.
     *
     * @return {@code true} if the conditions are satisfied, {@code false} otherwise.
     */
    public abstract boolean require(Worker worker);

    public EffectType getEffectType() {
        return effectType;
    }
}
