package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;
    protected Map<String, String> requirements;
    protected int enabled;

    @Override
    public abstract void apply(Worker activeWorker, Position position);

    @Override
    public abstract void prepare(Worker worker);

    @Override
    public abstract boolean require(Worker worker);

    @Override
    public abstract void clear(Worker worker);

}
