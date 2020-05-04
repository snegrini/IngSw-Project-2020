package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.EffectApplyMessage;
import it.polimi.ingsw.network.message.Message;

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
    public void apply(EffectApplyMessage message) {
        /*List<Worker> enemyWorkers = Game.getInstance().getEnemyWorkers(message.getWorker());
        for (Worker w : enemyWorkers) {
            w.addLockedMovement(moveType);
        }
        effect.apply(message);*/
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        if (moveType.equals(MoveType.UP)) {
            return worker.hasMovedUp() && effect.require(worker);
        }
        return false;
    }

}
