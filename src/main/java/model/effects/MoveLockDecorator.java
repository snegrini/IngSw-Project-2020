package model.effects;

import model.enumerations.EffectType;
import model.enumerations.MoveType;
import model.player.Worker;
import network.message.Move;

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
    public void apply(List<Worker> workers) {
        for (Worker w : workers) {
            w.addLockedMovement(MoveType.UP);
        }
    }

    @Override
    public boolean require(Worker worker) {
        return worker.hasMovedUp() && effect.require(worker);
    }

}
