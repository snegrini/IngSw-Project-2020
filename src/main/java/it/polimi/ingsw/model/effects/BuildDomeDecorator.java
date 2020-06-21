package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PositionMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Decorator to add a custom build to the simple effect. This allow to build a dome on any level.
 */
public class BuildDomeDecorator extends EffectDecorator {

    private static final long serialVersionUID = -7461449390585591178L;
    private List<Position> possibleBuilds;

    /**
     * Default constructor.
     *
     * @param effect       the effect to be decorated.
     * @param requirements the requirements (if any) which must be satisfied in order to apply the effect.
     */
    public BuildDomeDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.possibleBuilds = new ArrayList<>();

        setRequirements(requirements);
        setPhaseType(effect.getPhaseType());
        setTargetTypeMap(effect.getTargetTypeMap());
    }

    /**
     * Applies the effect to the given arguments.
     * Performs the custom build by building a dome on the space at the given position.
     *
     * @param activeWorker the active worker on which to activate the effect.
     * @param position     the position on which to perform the custom build.
     */
    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        Board board = Game.getInstance().getBoard();
        board.buildDomeForced(activeWorker, position);
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
        possibleBuilds = worker.getPossibleBuilds();

        // Note: spaces that have already a dome are automatically discarded by the getPossibleBuilds() method.
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
