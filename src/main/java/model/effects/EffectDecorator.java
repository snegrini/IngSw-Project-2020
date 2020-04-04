package model.effects;

import model.player.Worker;

import java.util.List;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;

    @Override
    public abstract void apply(List<Worker> targetWorkers);

    @Override
    public abstract boolean require(Worker worker);
}
