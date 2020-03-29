package model.effects;

import model.enumerations.EffectType;

public class SimpleEffect implements Effect {

    private EffectType effectType;

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
