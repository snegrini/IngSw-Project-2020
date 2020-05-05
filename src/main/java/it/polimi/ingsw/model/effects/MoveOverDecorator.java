package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

public class MoveOverDecorator extends EffectDecorator {

    private boolean pushBack;
    private boolean swapSpace;

    public MoveOverDecorator(Effect effect, Map<String, String> requirements, boolean pushBack, boolean swapSpace) {
        this.effect = effect;
        this.requirements = requirements;
        this.pushBack = pushBack;
        this.swapSpace = swapSpace;
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

        List<Position> possibleMoves = worker.getPossibleMoves();

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
        // TODO notifyObserver(); with a message
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
