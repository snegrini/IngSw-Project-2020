package model.effects;

import model.enumerations.EffectType;
import model.enumerations.MoveType;
import model.player.Worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MoveLockDecorator extends EffectDecorator {

    private MoveType moveType;

    public MoveLockDecorator(Effect effect, Map<String, String> requirements, MoveType moveType) {
        this.effect = effect;
        this.requirements = requirements;
        this.moveType = moveType;
    }

    @Override
    public void apply(List<Worker> targetWorkers) {

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
