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

/**
 * Decorator to add an extra build to the simple effect.
 */
public class BuildAgainDecorator extends EffectDecorator {

    private static final long serialVersionUID = 8442370103194172484L;

    private final boolean sameSpace;
    private final boolean dome;
    private final boolean forceSameSpace;

    private List<Position> possibleBuilds;

    /**
     * Default constructor.
     *
     * @param effect         the effect to be decorated.
     * @param requirements   the requirements (if any) which must be satisfied in order to apply the effect.
     * @param sameSpace      set to {@code true} to allow the extra build over the previous one, {@code false} to deny it.
     * @param dome           set to {@code true} to allow the extra build to be a dome, {@code false} to deny it.
     * @param forceSameSpace set to {@code true} to force the extra build to be on the same space as the previous one, {@code false} to deny it.
     */
    public BuildAgainDecorator(Effect effect, Map<String, String> requirements,
                               boolean sameSpace, boolean dome, boolean forceSameSpace) {
        this.effect = effect;
        this.sameSpace = sameSpace;
        this.dome = dome;
        this.forceSameSpace = forceSameSpace;
        this.possibleBuilds = new ArrayList<>();

        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    /**
     * Applies the effect to the given arguments.
     * Performs an extra build if the conditions are satisfied.
     *
     * @param activeWorker the active worker on which to activate the effect.
     * @param position     the position on which to perform the extra build.
     */
    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        if (possibleBuilds.contains(position)) {
            Board board = Game.getInstance().getBoard();
            board.buildBlock(activeWorker, position);
        } else {
            // TODO notify wrong position selected.
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

        // The possibleBuilds list has already been prepared by the require method.

        notifyObserver(new PositionMessage(Game.SERVER_NICKNAME, MessageType.BUILD_FX, possibleBuilds));
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
