package it.polimi.ingsw.model.effects;


import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.enumerations.XMLName;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.observer.Observable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class used to implement the Decorator Pattern for the effects.
 */
public abstract class Effect extends Observable implements Serializable {

    private static final long serialVersionUID = -2554121388314624548L;

    private PhaseType phaseType;
    private Map<XMLName, TargetType> targetTypeMap;

    protected Effect() {
        targetTypeMap = new HashMap<>();
    }

    /**
     * Applies the effect to the argument worker using the position argument.
     *
     * @param activeWorker the active worker.
     * @param position     the Position to apply the effect.
     */
    public abstract void apply(Worker activeWorker, Position position);

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

    /**
     * Clear the effect buffs or debuffs applied during the apply() method.
     *
     * @param worker the current worker.
     */
    public abstract void clear(Worker worker);

    public PhaseType getPhaseType() {
        return phaseType;
    }

    /**
     * Sets the effect type only if it hasn't already been set.
     * The attribute {@code phaseType} may not be modified again from decorators.
     *
     * @param phaseType the type of the effect.
     */
    protected void setPhaseType(PhaseType phaseType) {
        if (null == this.phaseType) {
            this.phaseType = phaseType;
        }
    }

    public TargetType getTargetType(XMLName xmlName) {
        return targetTypeMap.get(xmlName);
    }

    public void addTargetType(XMLName xmlName, TargetType targetType) {
        targetTypeMap.put(xmlName, targetType);
    }

    protected Map<XMLName, TargetType> getTargetTypeMap() {
        return targetTypeMap;
    }

    protected void setTargetTypeMap(Map<XMLName, TargetType> targetTypeMap) {
        this.targetTypeMap = targetTypeMap;
    }

    public abstract boolean isUserConfirmNeeded();
}
