package model.effects;
import model.enumerations.EffectType;



public class BuildDecorator extends EffectDecorator {
    private boolean sameSpace;
    private boolean buildDome;
    public void apply(){
        //TO DO

    }

    public boolean require(){
        //TO DO
    }

    public BuildDecorator(Effect effect, EffectType effectType, boolean sameSpace, boolean buildDome) {
        super(effect, effectType);
        this.sameSpace = sameSpace;
        this.buildDome = buildDome;
    }


    public boolean isSameSpace() {
        return sameSpace;
    }

    public boolean isBuildDome() {
        return buildDome;
    }
}
