package model.effects;

import model.player.Worker;

import java.util.List;
import java.util.Map;

public class WinDownDecorator extends EffectDecorator {

    private Map<String, String> requirements;

    public WinDownDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

}
