package model.effects;

import model.player.Worker;

import java.util.List;

public class WinDownDecorator extends EffectDecorator {

    private int numOfLevel;

    public WinDownDecorator(Effect effect, int numOfLevel) {
        this.effect = effect;
        this.numOfLevel = numOfLevel;
    }

    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    public boolean require(Worker worker) {
        // TODO
        return effect.require(worker);
    }

    public int getNumOfLevel() {
        return numOfLevel;
    }
}
