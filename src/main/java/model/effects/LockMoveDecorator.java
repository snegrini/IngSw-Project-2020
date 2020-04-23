package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class LockMoveDecorator extends EffectDecorator {

    public LockMoveDecorator(Effect effect, Map<String, String> requirements,
                             Map<String, String> parameters) {
        this.effect = effect;
        this.requirements = requirements;
        this.parameters = parameters;
    }

    public LockMoveDecorator(Effect effect, Map<String, String> parameters) {
        this(effect, null, parameters);
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

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
