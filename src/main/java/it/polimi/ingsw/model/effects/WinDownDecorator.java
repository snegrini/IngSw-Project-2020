package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.Map;

public class WinDownDecorator extends EffectDecorator {

    public WinDownDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(Worker activeWorker, Position position) {
        // TODO
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);
    }

    @Override
    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

    @Override
    public void clear(Worker worker) {
        effect.clear(worker);
    }
}
