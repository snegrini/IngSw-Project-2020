package model.effects;

import model.player.Worker;

import java.util.List;
import java.util.Map;

public class BuildDecorator extends EffectDecorator {

    private Map<String, String> parameters;

    public BuildDecorator(Effect effect, Map<String, String> parameters) {
        this.effect = effect;
        this.parameters = parameters;
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        effect.apply(targetWorkers);
    }

    @Override
    public boolean require(Worker worker) {
        return effect.require(worker);
    }
}
