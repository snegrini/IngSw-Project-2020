package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PositionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveOverDecorator extends EffectDecorator {

    private final boolean pushBack;
    private final boolean swapSpace;

    private List<Position> possibleMoves;

    public MoveOverDecorator(Effect effect, Map<String, String> requirements, boolean pushBack, boolean swapSpace) {
        this.effect = effect;
        this.requirements = requirements;
        this.pushBack = pushBack;
        this.swapSpace = swapSpace;
        this.possibleMoves = new ArrayList<>();
        setEffectType(effect.getEffectType());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        Board board = Game.getInstance().getBoard();
        Position activeWorkerPosition = activeWorker.getPosition();

        if (swapSpace) {
            Worker enemyWorker = board.getSpace(position).getWorker();
            board.getSpace(position).setWorker(enemyWorker);
            if (enemyWorker != null) {
                enemyWorker.move(activeWorkerPosition);
            }
            activeWorker.move(position);
        }

        if (pushBack) {
            Worker enemyWorker = board.getSpace(position).getWorker();
            board.getSpace(position).setWorker(enemyWorker);
            if (enemyWorker != null) {
                enemyWorker.move(activeWorkerPosition);
            }
            activeWorker.move(position);
        }
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        // The possibleMoves list has already been prepared by the require method.

        notifyObserver(new PositionMessage(Game.SERVER_NICKNAME, MessageType.BUILD_FX, possibleMoves));
    }

    @Override
    public boolean require(Worker worker) {
        possibleMoves = worker.getPossibleMoves();

        Board board = Game.getInstance().getBoard();
        List<Position> adjOpponentPos = board.getNeighbourWorkers(worker.getPosition(), true);

        if (swapSpace) {
            possibleMoves.addAll(adjOpponentPos);
        }

        if (pushBack) {
            for (Position oppPos : adjOpponentPos) {
                Space space = board.getNextSpaceInLine(worker.getPosition(), oppPos);
                if (space.isFree()) {
                    possibleMoves.add(oppPos);
                }
            }
        }

        return !possibleMoves.isEmpty() && effect.require(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
