package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.ErrorMessage;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PositionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Decorator to add a custom move to the simple effect.
 */
public class MoveOverDecorator extends EffectDecorator {

    private static final long serialVersionUID = -4280308808871559027L;
    private final boolean pushBack;
    private final boolean swapSpace;

    private List<Position> possibleMoves;

    /**
     * Default constructor.
     *
     * @param effect       the effect to be decorated.
     * @param requirements the requirements (if any) which must be satisfied in order to apply the effect.
     * @param pushBack     set to {@code true} to allow the custom move to push an adjacent enemy worker on the next available space (in-line), {@code false} to deny it.
     * @param swapSpace    set to {@code true} to allow the custom move to swap the position with an adjacent enemy worker, {@code false} to deny it.
     */
    public MoveOverDecorator(Effect effect, Map<String, String> requirements, boolean pushBack, boolean swapSpace) {
        this.effect = effect;
        this.pushBack = pushBack;
        this.swapSpace = swapSpace;
        this.possibleMoves = new ArrayList<>();

        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        if (!possibleMoves.contains(position)) {
            notifyObserver(new ErrorMessage(Game.SERVER_NICKNAME, "Bad position given."));
            return;
        }

        Board board = Game.getInstance().getBoard();

        if (swapSpace) {
            Worker enemyWorker = board.getSpace(position).getWorker();
            if (enemyWorker != null) {
                board.swapWorkers(activeWorker, enemyWorker);
            } else {
                board.moveWorker(activeWorker, position);
            }
        }

        if (pushBack) {
            Worker enemyWorker = board.getSpace(position).getWorker();

            if (enemyWorker != null) {
                Position nextPos = board.getNextPositionInLine(activeWorker.getPosition(), position);
                Space nextSpace = board.getSpace(nextPos);
                enemyWorker.move(nextPos);
                nextSpace.setWorker(enemyWorker);
            }
            board.moveWorker(activeWorker, position);
        }
    }

    /**
     * Prepares the argument worker in order to apply the effect.
     * Notifies the views in order to retrieve the needed information to apply the effect.
     *
     * @param worker the worker to prepare.
     */
    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        // The possibleMoves list has already been prepared by the require method.

        notifyObserver(new PositionMessage(Game.SERVER_NICKNAME, MessageType.MOVE_FX, possibleMoves));
    }

    @Override
    public boolean require(Worker worker) {
        possibleMoves = worker.getPossibleMoves();

        Board board = Game.getInstance().getBoard();
        Space currentSpace = board.getSpace(worker.getPosition());

        List<Position> adjOpponentPos = board.getNeighbourWorkers(worker.getPosition(), true);
        adjOpponentPos = worker.filterLockedMovementPositions(adjOpponentPos);

        if (swapSpace) {
            possibleMoves.addAll(adjOpponentPos);

            possibleMoves = possibleMoves.stream()
                    .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) <= currentSpace.getLevel())
                    .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) >= -1)
                    .collect(Collectors.toList());
        }

        if (pushBack) {
            for (Position oppPos : adjOpponentPos) {
                Space space = board.getNextSpaceInLine(worker.getPosition(), oppPos);
                if (space != null && space.isFree()) {
                    possibleMoves.add(oppPos);
                }
            }

            possibleMoves = possibleMoves.stream()
                    .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) <= currentSpace.getLevel())
                    .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) >= -1)
                    .collect(Collectors.toList());
        }

        // Effect is applicable only if there are adjacent enemy workers in non-locked-movement positions.
        return !adjOpponentPos.isEmpty() && effect.require(worker);
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
