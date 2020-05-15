package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.WinMessage;

import java.util.Map;

public class MoveWinDecorator extends EffectDecorator {

    private final MoveType moveType;
    private final int levels;

    public MoveWinDecorator(Effect effect, Map<String, String> requirements,
                            MoveType moveType, int levels) {
        this.effect = effect;
        this.requirements = requirements;
        this.moveType = moveType;
        this.levels = levels;
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
                notifyObserver(new WinMessage());
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
