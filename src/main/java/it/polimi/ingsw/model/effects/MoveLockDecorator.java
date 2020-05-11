package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.XMLName;
import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

public class MoveLockDecorator extends EffectDecorator {

    private final MoveType lockMoveType;

    public MoveLockDecorator(Effect effect, Map<String, String> requirements, MoveType lockMoveType) {
        this.effect = effect;
        this.requirements = requirements;
        this.lockMoveType = lockMoveType;
        setPhaseType(effect.getPhaseType());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);
        enabled = 2;

        List<Worker> targetWorkers = Game.getInstance().getWorkersByTargetType(activeWorker, getTargetType(XMLName.PARAMETERS));
        for (Worker w : targetWorkers) {
            w.addLockedMovement(lockMoveType);
        }
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        MoveType moveTypeRequired = MoveType.valueOf(requirements.get(XMLName.MOVE.getText()));
        if (moveTypeRequired.equals(MoveType.UP)) {
            return worker.hasMovedUp() && effect.require(worker);
        }
        return false;
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);

        if (enabled == 2) {
            enabled--;
        } else if (enabled == 1) {
            List<Worker> enemyWorkers = Game.getInstance().getEnemyWorkers(worker);
            for (Worker w : enemyWorkers) {
                w.removeLockedMovement(lockMoveType);
            }
            enabled--;
        }
    }
}
