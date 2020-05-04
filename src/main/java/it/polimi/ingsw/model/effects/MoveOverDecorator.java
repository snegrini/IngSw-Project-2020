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
    public void apply(Worker worker) {
        effect.apply(worker);

        // TODO return list of possibleMoves or at least notify the it.polimi.ingsw.controller in any way.
    }


    @Override
    public boolean require(Worker worker) {
        List<Position> possibleMoves = worker.getPossibleMoves();

        Board board = Game.getInstance().getBoard();
        List<Position> adjOpponentPos = board.getNeighbourWorkers(worker.getPosition(), true);

        if (pushBack) {
            possibleMoves.addAll(adjOpponentPos);
        }

        if (swapSpace) {
            for (Position oppPos : adjOpponentPos) {
                Space space = board.getNextSpaceInLine(worker.getPosition(), oppPos);
                if (space.isFree()) {
                    possibleMoves.add(oppPos);
                }
            }
        }

        return effect.require(worker);
    }
}
