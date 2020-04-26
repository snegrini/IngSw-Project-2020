package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;

import java.util.List;
import java.util.Map;

public class WinDownDecorator extends EffectDecorator {

    public WinDownDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
