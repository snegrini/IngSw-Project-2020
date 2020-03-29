package model.effects;

import model.enumerations.EffectType;

public class LockMoveDecorator extends EffectDecorator{

    private int numOfLevel;
    public void apply(){
        //TO DO
    }
    public boolean require(){
        //TO DO

    }

    public LockMoveDecorator(int numOfLevel) {
        this.numOfLevel = numOfLevel;
    }

    public int getNumOfLevel() {
        return numOfLevel;
    }
}
