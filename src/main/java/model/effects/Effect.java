package model.effects;


import model.enumerations.EffectType;

/**
 * Abstract class used to implement the Decorator Pattern for the effects.
 */
public abstract class Effect {

    protected EffectType effectType;

    /**
     *  Applies the effect.
     */
    public abstract void apply();

    /**
     * Checks the necessary conditions for the effect to be applied.
     *
     * @return {@code true} if the conditions are satisfied, {@code false} otherwise.
     */
    public abstract boolean require();

    public EffectType getEffectType() {
        return effectType;
    }
}
