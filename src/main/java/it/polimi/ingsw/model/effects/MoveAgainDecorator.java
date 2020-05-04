package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.EffectApplyMessage;
import it.polimi.ingsw.network.message.Message;

public class MoveAgainDecorator extends EffectDecorator {

    private int quantity;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int quantity, boolean goBack) {
        this.effect = effect;
        this.quantity = quantity;
        this.goBack = goBack;
    }

    @Override
    public void apply(EffectApplyMessage message) {
        effect.apply(message);
    }

    @Override
    public void prepare(Worker worker) {
        effect.prepare(worker);

        if (!goBack) {
            worker.addLockedMovement(MoveType.BACK);
        }
    }

    @Override
    public boolean require(Worker worker) {
        return effect.require(worker);
    }
}
