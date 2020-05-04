package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

public class MoveLockDecorator extends EffectDecorator {

    private MoveType moveType;

    public MoveLockDecorator(Effect effect, Map<String, String> requirements, MoveType moveType) {
        this.effect = effect;
        this.requirements = requirements;
        this.moveType = moveType;
    }

    @Override
    public void apply(Worker activeWorker) {
        // TODO retrieve enemy workers from the game
        /*Game.getInstance().getBoard().
        for (Worker w : workers) {
            w.addLockedMovement(moveType);
        }*/
    }

    @Override
    public boolean require(Worker worker) {
        if (moveType.equals(MoveType.UP)) {
            return worker.hasMovedUp() && effect.require(worker);
        }
        return false;
    }

}
