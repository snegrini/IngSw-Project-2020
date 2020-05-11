package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Worker;

public class SimpleEffect extends Effect {

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

}
