package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Worker;

/**
 * Base effect class that can be decorated with any {@link EffectDecorator}.
 */
public class SimpleEffect extends Effect {

    private static final long serialVersionUID = 6467254688715458424L;

    /**
     * Default constructor
     *
     * @param phaseType Phase Type of the Effect.
     */
    public SimpleEffect(PhaseType phaseType) {
        super.setPhaseType(phaseType);
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        // Do nothing.
    }

    @Override
    public void prepare(Worker worker) {
        // Do nothing.
    }

    @Override
    public boolean require(Worker worker) {
        // Always satisfied.
        return true;
    }

    @Override
    public void clear(Worker worker) {
        // Do nothing.
    }

    @Override
    public boolean isUserConfirmNeeded() {
        return false;
    }
}
