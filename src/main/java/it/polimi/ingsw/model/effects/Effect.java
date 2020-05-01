package it.polimi.ingsw.model.effects;


import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.enumerations.XMLName;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.observer.Observable;

import java.util.HashMap;
import java.util.List;
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
     * Applies the effect to the argument workers.
     *
     * @param targetWorkers the workers to whose apply the effect.
     */
    public abstract void apply(List<Worker> targetWorkers);

    /**
     * Prepare the argument worker in order to apply the effect.
     *
     * @param worker the worker to prepare.
     */
    public abstract void prepare(Worker worker);

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
