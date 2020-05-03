package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.player.Worker;

import java.util.Map;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;
    protected Map<String, String> requirements;

    @Override
    public abstract void apply(Worker worker);

    @Override
    public abstract boolean require(Worker worker);
}
