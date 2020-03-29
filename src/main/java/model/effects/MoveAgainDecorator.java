package model.effects;

import model.enumerations.EffectType;

public class MoveAgainDecorator extends EffectDecorator {

    private int numOfMoves;
    private boolean goBack;
    public void apply(){
        // TO DO
    }

    public boolean require() {
        //TO DO
    }

    public MoveAgainDecorator(int numOfMoves, boolean goBack) {
        this.numOfMoves = numOfMoves;
        this.goBack = goBack;
    }

    public int getNumOfMoves() {
        return numOfMoves;
    }

    public boolean isGoBack() {
        return goBack;
    }
}
