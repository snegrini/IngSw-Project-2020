package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;

import java.util.List;
import java.util.Map;

public class BuildDecorator extends EffectDecorator {

    private Map<String, String> requirements;
    private Map<String, String> parameters;

    public BuildDecorator(Effect effect, Map<String, String> requirements,
                          Map<String, String> parameters) {
        this.effect = effect;
        this.parameters = parameters;
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

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
