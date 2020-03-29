package model.effects;

import model.enumerations.EffectType;

public class MoveOverDecorator extends EffectDecorator {
    private boolean swapSpace;
    public void apply(){
        // TO DO
    }
    public boolean require(){
        // TO DO
    }

    public MoveOverDecorator(boolean swapSpace) {
        this.swapSpace = swapSpace;
    }

    public boolean isSwapSpace() {
        return swapSpace;
    }
}
