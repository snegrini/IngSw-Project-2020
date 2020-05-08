package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

public class WinMoveDecorator extends EffectDecorator {

    private MoveType moveType;
    private int levels;

    public WinMoveDecorator(Effect effect, Map<String, String> requirements,
                            MoveType moveType, int levels) {
        this.effect = effect;
        this.requirements = requirements;
        this.moveType = moveType;
        this.levels = levels;
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        if (moveType == MoveType.DOWN && activeWorker.hasMovedDown()) {
            int currentLvl = activeWorker.getLevel();
            int lastLvl = activeWorker.getHistory().getMoveLevel();
            if (currentLvl < lastLvl - levels) {
                // TODO notifyObservers()
                //      The player of the active workers has won the game.
                //      Game is finished.
            }

        }
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        return effect.require(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
