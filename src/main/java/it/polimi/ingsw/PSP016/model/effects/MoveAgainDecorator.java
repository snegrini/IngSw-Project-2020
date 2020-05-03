package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.enumerations.MoveType;
import it.polimi.ingsw.PSP016.model.player.Worker;

public class MoveAgainDecorator extends EffectDecorator {

    private int quantity;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int quantity, boolean goBack) {
        this.effect = effect;
        this.quantity = quantity;
        this.goBack = goBack;
    }

    @Override
    public void apply(Worker worker) {
        effect.apply(worker);

        if (!goBack) {
            worker.addLockedMovement(MoveType.BACK);
        }
    }

    @Override
    public boolean require(Worker worker) {
        return effect.require(worker);
    }
}
