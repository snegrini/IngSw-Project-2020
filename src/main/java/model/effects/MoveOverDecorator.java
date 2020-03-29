package model.effects;

import model.Game;

public class MoveOverDecorator extends EffectDecorator {

    private boolean swapSpace;

    public MoveOverDecorator(Effect effect, boolean swapSpace) {
        this.effect = effect;
        this.swapSpace = swapSpace;
    }

    public void apply(){
        // TODO
    }
    public boolean require() {
        // TODO: get the current worker. Check if in his range
        //       there are enemies' workers.
        //       If there are, then another condition need to be checked:
        //          If swapSpace == true, then the effect is applicable.
        //          Otherwise it is necessary to check the backwards space
        //          (see Minotaur effect for more info).
        //       Otherwise effect is not applicable.
    }

    public boolean isSwapSpace() {
        return swapSpace;
    }
}
