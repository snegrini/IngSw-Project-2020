package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;

import java.util.List;
import java.util.Map;

public class BuildDomeDecorator extends EffectDecorator {

    public BuildDomeDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        // TODO
        effect.apply(targetWorkers);
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
