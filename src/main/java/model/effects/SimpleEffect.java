package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;

import java.util.List;

public class SimpleEffect extends Effect {

    public SimpleEffect(EffectType effectType) {
        super.setEffectType(effectType);
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        // DO NOTHING.
    }

    @Override
    public boolean require(Worker worker) {
        return true;
    }
}
