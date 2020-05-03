package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.player.Worker;

import java.util.Map;

public class BuildDomeDecorator extends EffectDecorator {

    public BuildDomeDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(Worker worker) {
        effect.apply(worker);
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
