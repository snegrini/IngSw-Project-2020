package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.enumerations.EffectType;
import it.polimi.ingsw.PSP016.model.player.Worker;

public class SimpleEffect extends Effect {

    public SimpleEffect(EffectType effectType) {
        super.setEffectType(effectType);
    }


    @Override
    public void apply(Worker worker) {
        // DO NOTHING.
    }

    @Override
    public boolean require(Worker worker) {
        return true;
    }
}
