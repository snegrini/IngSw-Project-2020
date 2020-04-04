package model.effects;

import model.player.Worker;

import java.util.List;

public class BuildDecorator extends EffectDecorator {
    private boolean sameSpace;
    private boolean buildDome;

    public BuildDecorator(Effect effect, boolean sameSpace, boolean buildDome) {
        this.effect = effect;
        this.sameSpace = sameSpace;
        this.buildDome = buildDome;
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        effect.apply(targetWorkers);
    }

    @Override
    public boolean require(Worker worker) {
        return effect.require(worker);
    }


    public boolean isSameSpace() {
        return sameSpace;
    }

    public boolean isBuildDome() {
        return buildDome;
    }
}
