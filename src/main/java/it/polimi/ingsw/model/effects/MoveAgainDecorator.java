package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PositionMessage;

import java.util.ArrayList;
import java.util.List;

public class MoveAgainDecorator extends EffectDecorator {

    private final int quantity;
    private final boolean goBack;

    private List<Position> possibleMoves;

    public MoveAgainDecorator(Effect effect, int quantity, boolean goBack) {
        this.effect = effect;
        this.quantity = quantity;
        this.goBack = goBack;
        this.possibleMoves = new ArrayList<>();
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);
        activeWorker.move(position);
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        // The possibleMoves list has already been prepared by the require method.

        notifyObserver(new PositionMessage(Game.SERVER_NICKNAME, MessageType.BUILD_FX, possibleMoves));
    }

    /**
     * Checks the necessary conditions for the effect to be applied.
     * The possible moves of the worker are evaluated and compared with the requirements.
     *
     * @param worker The active worker of the player that is currently playing.
     * @return {@code true} if the effect requirements are satisfied, {@code false} otherwise.
     */
    @Override
    public boolean require(Worker worker) {
        possibleMoves = worker.getPossibleMoves();

        if (!goBack) {
            possibleMoves.removeIf(position -> position.equals(worker.getHistory().getMovePosition()));
        }

        return !possibleMoves.isEmpty() && effect.require(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
