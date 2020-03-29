package model.effects;

public class BuildDecorator extends EffectDecorator {
    private boolean sameSpace;
    private boolean buildDome;

    public BuildDecorator(Effect effect, boolean sameSpace, boolean buildDome) {
        this.effect = effect;
        this.sameSpace = sameSpace;
        this.buildDome = buildDome;
    }

    @Override
    public void apply() {
        effect.apply();
    }

    @Override
    public boolean require() {
        return effect.require();
    }


    public boolean isSameSpace() {
        return sameSpace;
    }

    public boolean isBuildDome() {
        return buildDome;
    }
}
