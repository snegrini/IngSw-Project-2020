package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.List;

public class MoveAgainDecorator extends EffectDecorator {

    private int quantity;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int quantity, boolean goBack) {
        this.effect = effect;
        this.quantity = quantity;
        this.goBack = goBack;
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        if (!goBack) {
            List<Position> possibleMoves = worker.getPossibleMoves();
            possibleMoves.removeIf(pos -> pos.equals(worker.getHistory().getMovePosition()));
        }
        // TODO notifyObserver()
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
