package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.WinMessage;

import java.util.Map;

/**
 * Decorator to add a move win condition to the simple effect.
 * This allows a player to win by satisfying the given move condition.
 */
public class MoveWinDecorator extends EffectDecorator {
    private static final long serialVersionUID = -6797320098115774664L;

    private final MoveType moveType;
    private final int levels;

    /**
     * Default constructor.
     *
     * @param effect       the effect to be decorated.
     * @param requirements the requirements (if any) which must be satisfied in order to apply the effect.
     * @param moveType     the movement type which must be performed in order to win by using this effect.
     * @param levels       the number of levels which must be satisfied in order to apply this effect.
     */
    public MoveWinDecorator(Effect effect, Map<String, String> requirements,
                            MoveType moveType, int levels) {
        this.effect = effect;
        this.moveType = moveType;
        this.levels = levels;

        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        if (moveType == MoveType.DOWN && activeWorker.hasMovedDown()) {
            int currentLvl = activeWorker.getLevel();
            int lastLvl = activeWorker.getHistory().getMoveLevel();
            if (lastLvl - currentLvl >= levels) {
                // The player of the active workers has won the game.
                // Game is finished.

                //nickname will be setted by controller.
                notifyObserver(new WinMessage(null));
            }
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
        return effect.require(worker);
    }

    /**
     * Clears the effect buffs or debuffs applied during the apply() method.
     *
     * @param worker the current worker.
     */
    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
