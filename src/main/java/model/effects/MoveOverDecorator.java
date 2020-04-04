package model.effects;

import model.Game;
import model.board.Board;
import model.player.Worker;

import java.util.List;

public class MoveOverDecorator extends EffectDecorator {

    private boolean swapSpace;

    public MoveOverDecorator(Effect effect, boolean swapSpace) {
        this.effect = effect;
        this.swapSpace = swapSpace;
    }

    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    public boolean require(Worker worker) {
        // TODO: get the current worker. Check if in his range
        //       there are enemies' workers.
        //       If there are, then another condition needs to be checked:
        //          If swapSpace == true, then the effect is applicable.
        //          Otherwise it is necessary to check the backwards space
        //          (see Minotaur effect for more info).
        //       Otherwise effect is not applicable.
        return effect.require(worker);
    }

    public boolean isSwapSpace() {
        return swapSpace;
    }
}
