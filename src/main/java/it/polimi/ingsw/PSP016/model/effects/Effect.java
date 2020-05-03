package it.polimi.ingsw.PSP016.model.effects;


import it.polimi.ingsw.PSP016.model.enumerations.EffectType;
import it.polimi.ingsw.PSP016.model.enumerations.TargetType;
import it.polimi.ingsw.PSP016.model.enumerations.XMLName;
import it.polimi.ingsw.PSP016.model.player.Worker;
import it.polimi.ingsw.PSP016.observer.Observable;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class used to implement the Decorator Pattern for the effects.
 */
public abstract class Effect extends Observable {

    private EffectType effectType;
    private Map<XMLName, TargetType> targetTypeMap;

    protected Effect() {
        targetTypeMap = new HashMap<>();
    }

    /**
     * Applies the effect to the argument worker.
     *
     * @param worker the workers to whose apply the effect.
     */
    public abstract void apply(Worker worker);

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
    protected void setEffectType(EffectType effectType) {
        if (null == this.effectType) {
            this.effectType = effectType;
        }
    }

    public TargetType getTargetType(XMLName xmlName) {
        return targetTypeMap.get(xmlName);
    }

    public void addTargetType(XMLName xmlName, TargetType targetType) {
        targetTypeMap.put(xmlName, targetType);
    }
}
