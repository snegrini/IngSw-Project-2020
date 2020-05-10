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

public class BuildDomeDecorator extends EffectDecorator {

    private List<Position> possibleBuilds;

    public BuildDomeDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
        this.possibleBuilds = new ArrayList<>();
        setEffectType(effect.getEffectType());
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);

        Board board = Game.getInstance().getBoard();
        board.buildDomeForced(activeWorker, position);
    }

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

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
