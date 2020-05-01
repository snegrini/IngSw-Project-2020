package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;
    protected Map<String, String> requirements;

    @Override
    public abstract void apply(List<Worker> workers);

    @Override
    public abstract void prepare(Worker worker);

    @Override
    public abstract boolean require(Worker worker);
}
