package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.player.Worker;

import java.util.Map;

public class WinDownDecorator extends EffectDecorator {

    public WinDownDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(Worker worker) {
        // TODO
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
