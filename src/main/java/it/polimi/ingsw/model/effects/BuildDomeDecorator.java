package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.player.Worker;

import java.util.List;
import java.util.Map;

public class BuildDomeDecorator extends EffectDecorator {

    public BuildDomeDecorator(Effect effect, Map<String, String> requirements) {
        this.effect = effect;
        this.requirements = requirements;
    }

    @Override
    public void apply(List<Worker> workers) {
        Worker worker = workers.get(0);

        effect.apply(workers);
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

}
