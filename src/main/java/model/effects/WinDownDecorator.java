package model.effects;

import model.enumerations.EffectType;

public class WinDownDecorator extends EffectDecorator {

    private int numOfLevel;
    public void apply(){
        // TO DO
    }

    public boolean require(){
        //TO DO
    }

    public WinDownDecorator(EffectType effectType, int numOfLevel) {
        super(effectType);
        this.numOfLevel = numOfLevel;
    }

    public int getNumOfLevel() {
        return numOfLevel;
    }
}
