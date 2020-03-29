package model.effects;


/**
 * Interface used to implement the Decorator Pattern for the effects.
 */
public interface Effect {

    /**
     *  Applies the effect.
     */
    void apply();

    /**
     * Checks the necessary conditions for the effect to be applied.
     *
     * @return {@code true} if the conditions are satisfied, {@code false} otherwise.
     */
    boolean require();
}
