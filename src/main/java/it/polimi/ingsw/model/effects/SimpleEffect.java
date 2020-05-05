package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.player.Worker;

public class SimpleEffect extends Effect {

    public SimpleEffect(EffectType effectType) {
        super.setEffectType(effectType);
    }


    @Override
    public void apply(Worker activeWorker, Position position) {
        // DO NOTHING.
    }

    @Override
    public void prepare(Worker worker) {
        // DO NOTHING.
    }

    @Override
    public boolean require(Worker worker) {
        return true;
    }

    @Override
    public void clear(Worker worker) {
        // DO NOTHING.
    }

}
