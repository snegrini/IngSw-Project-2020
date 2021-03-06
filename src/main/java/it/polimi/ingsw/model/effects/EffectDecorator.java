package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.XMLName;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

/**
 * Parent decorator for the Effect class. Used to add custom effects to a SimpleEffect.
 */
public abstract class EffectDecorator extends Effect {

    private static final long serialVersionUID = -7206957104654092952L;

    protected Effect effect;
    protected int enabled;
    private Map<String, String> requirements;

    @Override
    public abstract void apply(Worker activeWorker, Position position);

    @Override
    public abstract void prepare(Worker worker);

    @Override
    public abstract boolean require(Worker worker);

    @Override
    public abstract void clear(Worker worker);

    @Override
    public boolean isUserConfirmNeeded() {
        return Boolean.parseBoolean(requirements.getOrDefault(XMLName.USERCONFIRM.getText(), "true"));
    }

    /**
     * Set requirements of Effect.
     *
     * @param requirements requirements of Effect.
     */
    protected void setRequirements(Map<String, String> requirements) {
        this.requirements = requirements;
    }

    /**
     * Get Requirements of Effect.
     *
     * @return Requirements of Effect.
     */
    protected Map<String, String> getRequirements() {
        return requirements;
    }
}
