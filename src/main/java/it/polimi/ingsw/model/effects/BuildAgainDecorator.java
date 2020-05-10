package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuildAgainDecorator extends EffectDecorator {

    private final int quantity;
    private final boolean sameSpace;
    private final boolean dome;
    private final boolean forceSameSpace;

    private List<Position> possibleBuilds;

    public BuildAgainDecorator(Effect effect, Map<String, String> requirements,
                               int quantity, boolean sameSpace, boolean dome, boolean forceSameSpace) {
        this.effect = effect;
        this.requirements = requirements;
        this.quantity = quantity;
        this.sameSpace = sameSpace;
        this.dome = dome;
        this.forceSameSpace = forceSameSpace;
        this.possibleBuilds = new ArrayList<>();
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        if (possibleBuilds.contains(position)) {
            Board board = Game.getInstance().getBoard();
            board.buildBlock(activeWorker, position);
        }
        // TODO notify wrong position selected.
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        // The possibleBuilds list has already been prepared by the require method.

        // TODO notifyObserver()
    }

    @Override
    public boolean require(Worker worker) {
        Board board = Game.getInstance().getBoard();
        possibleBuilds = worker.getPossibleBuilds();

        if (forceSameSpace) {
            Position lastBuiltPosition = worker.getHistory().getBuildPosition();
            Space lastBuiltSpace = board.getSpace(lastBuiltPosition);
            possibleBuilds.removeIf(p -> !p.equals(lastBuiltPosition));

            return !lastBuiltSpace.hasDome() && lastBuiltSpace.getLevel() < 3
                    && effect.require(worker);
        }

        if (!sameSpace) {
            possibleBuilds.removeIf(p -> p.equals(worker.getHistory().getBuildPosition()));
        }

        if (!dome) {
            // Remove positions where a new build would be a dome.
            // Note: spaces that have already a dome are automatically discarded by the getPossibleBuilds() method.
            possibleBuilds.removeIf(p -> board.getSpace(p).getLevel() == 3);
        }

        return !possibleBuilds.isEmpty() && effect.require(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
