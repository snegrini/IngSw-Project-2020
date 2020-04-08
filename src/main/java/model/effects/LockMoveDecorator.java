package model.effects;

import model.player.Worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class LockMoveDecorator extends EffectDecorator {

    private Map<String, String> requirements;
    private Map<String, String> parameters;

    public LockMoveDecorator(Effect effect, Map<String, String> requirements,
                             Map<String, String> parameters) {
        this.effect = effect;
        this.requirements = requirements;
        this.parameters = parameters;
    }

    public LockMoveDecorator(Effect effect, Map<String, String> parameters) {
        this(effect, null, parameters);
    }

    public void apply(List<Worker> targetWorkers) {
        // TODO
    }
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }


}
