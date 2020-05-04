package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.EffectApplyMessage;
import it.polimi.ingsw.network.message.Message;

public class SimpleEffect extends Effect {

    public SimpleEffect(EffectType effectType) {
        super.setEffectType(effectType);
    }


    @Override
    public void apply(EffectApplyMessage message) {
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
}
