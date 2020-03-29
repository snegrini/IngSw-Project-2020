package model.effects;

public abstract class EffectDecorator implements Effect {

    protected Effect effect;

    @Override
    public abstract void apply();

    @Override
    public abstract boolean require();
}
