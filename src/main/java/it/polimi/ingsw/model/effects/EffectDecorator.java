package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.EffectApplyMessage;
import it.polimi.ingsw.network.message.Message;

import java.util.Map;

public abstract class EffectDecorator extends Effect {

    protected Effect effect;
    protected Map<String, String> requirements;

    @Override
    public abstract void apply(EffectApplyMessage message);

    @Override
    public abstract void prepare(Worker worker);

    @Override
    public abstract boolean require(Worker worker);
}
