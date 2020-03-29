package model.effects;
import model.enumerations.EffectType;

public abstract class EffectDecorator implements Effect{

    protected Effect effect;

    public EffectType effectType;



    public void apply(){

    }

    public abstract boolean require();

    public EffectDecorator(Effect effect, EffectType effectType) {
        this.effect = effect;
        this.effectType = effectType;
    }

    public Effect getEffect() {
        return effect;
    }

    public EffectType getEffectType() {
        return effectType;
    }
}
