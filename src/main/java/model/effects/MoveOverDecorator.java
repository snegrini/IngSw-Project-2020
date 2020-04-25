package model.effects;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.board.Space;
import model.enumerations.EffectType;
import model.enumerations.XMLName;
import model.player.Worker;

import java.util.List;
import java.util.Map;

public class MoveOverDecorator extends EffectDecorator {

    private Map<String, String> requirements;
    private boolean pushBack;
    private boolean swapSpace;

    public MoveOverDecorator(Effect effect, Map<String, String> requirements, boolean pushBack, boolean swapSpace) {
        this.effect = effect;
        this.requirements = requirements;
        this.pushBack = pushBack;
        this.swapSpace = swapSpace;
    }

    @Override
    public void apply(List<Worker> workers) {
        effect.apply(workers);


        // TODO return list of possibleMoves or at least notify the controller in any way.

    }

    @Override
    public boolean require(Worker worker) {
        List<Position> possibleMoves = worker.getPossibleMoves();

        Board board = Game.getInstance().getBoard();
        List<Position> adjOpponentPos = board.getNeighbourWorkers(worker.getPosition());

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

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
