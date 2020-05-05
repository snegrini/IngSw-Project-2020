package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

public class BuildAgainDecorator extends EffectDecorator {

    public BuildAgainDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        effect.apply(activeWorker, position);
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }


}
