package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.enumerations.XMLName;
import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

/**
 * Decorator to add a move lock to the simple effect.
 * This allows to lock a worker movement.
 */
public class MoveLockDecorator extends EffectDecorator {

    private static final long serialVersionUID = 2288855472114446569L;

    private final MoveType lockMoveType;
    private List<Worker> targetWorkers;

    /**
     * Default constructor.
     *
     * @param effect       the effect to be decorated.
     * @param requirements the requirements (if any) which must be satisfied in order to apply the effect.
     * @param lockMoveType the movement type which must be locked.
     */
    public MoveLockDecorator(Effect effect, Map<String, String> requirements, MoveType lockMoveType) {
        this.effect = effect;
        this.lockMoveType = lockMoveType;

        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        if (getTargetType(XMLName.PARAMETERS) == TargetType.ALL_OPP_WORKERS) {
            enabled = 2;
        } else {
            enabled = 1;
        }

        targetWorkers = Game.getInstance().getWorkersByTargetType(activeWorker, getTargetType(XMLName.PARAMETERS));
        for (Worker w : targetWorkers) {
            w.addLockedMovement(lockMoveType);
        }
    }

    /**
     * Prepares the argument worker in order to apply the effect.
     *
     * @param worker the worker to prepare.
     */
    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {

        clear(worker); // Clear previous applied effect.

        String moveTypeStr = getRequirements().get(XMLName.MOVE.getText());
        if (moveTypeStr != null) {
            MoveType moveTypeRequired = MoveType.valueOf(moveTypeStr);
            if (moveTypeRequired.equals(MoveType.UP)) {
                return worker.hasMovedUp() && effect.require(worker);
            } else if (moveTypeRequired.equals(MoveType.NONE)) {
                return effect.require(worker);
            }
        }
        return false;
    }

    /**
     * Clears the effect buffs or debuffs applied during the apply() method.
     *
     * @param worker the current worker.
     */
    @Override
    public void clear(Worker worker) {

        if (enabled == 2) {
            enabled--;
        } else if (enabled == 1 && targetWorkers != null) {
            // The targetWorkers list should not be null since enabled is 1. This means the method apply has been called.
            for (Worker w : targetWorkers) {
                w.removeLockedMovement(lockMoveType);
            }
            enabled--;
        }
    }
}
