package model.effects;

import model.player.Worker;

import java.util.List;
import java.util.Map;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;

    protected Map<String, String> requirements;
    protected Map<String, String> parameters;

    @Override
    public abstract void apply(List<Worker> targetWorkers);

    @Override
    public abstract boolean require(Worker worker);
}
