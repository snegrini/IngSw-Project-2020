package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PositionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveAgainDecorator extends EffectDecorator {

    private static final long serialVersionUID = -7009125378905883696L;
    private final boolean goBack;

    private List<Position> possibleMoves;

    public MoveAgainDecorator(Effect effect, Map<String, String> requirements, boolean goBack) {
        this.effect = effect;
        this.goBack = goBack;
        this.possibleMoves = new ArrayList<>();
        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);
        Game.getInstance().moveWorker(activeWorker, position);
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        // The possibleMoves list has already been prepared by the require method.

        notifyObserver(new PositionMessage(Game.SERVER_NICKNAME, MessageType.MOVE_FX, possibleMoves));
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
