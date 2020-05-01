package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

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
            w.addLockedMovement(moveType);
        }
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        return worker.hasMovedUp() && effect.require(worker);
    }

}
