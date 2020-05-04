package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.EffectApplyMessage;
import it.polimi.ingsw.network.message.Message;

import java.util.Map;

public class WinDownDecorator extends EffectDecorator {

    public WinDownDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(EffectApplyMessage message) {
        // TODO
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
