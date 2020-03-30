package model.effects;

import model.enumerations.EffectType;

public class SimpleEffect extends Effect {

    public SimpleEffect(EffectType effectType) {
        this.effectType = effectType;
    }

    @Override
    public void apply() {
        // DO NOTHING.
    }

    @Override
    public boolean require() {
        return true;
    }
}
