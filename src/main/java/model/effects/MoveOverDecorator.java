package model.effects;

import model.Game;
import model.board.Board;
import model.enumerations.EffectType;
import model.enumerations.XMLName;
import model.player.Worker;

import java.util.List;
import java.util.Map;

public class MoveOverDecorator extends EffectDecorator {

    public MoveOverDecorator(Effect effect, Map<String, String> requirements,
                             Map<String, String> parameters) {
        this.effect = effect;
        this.requirements = requirements;
        this.parameters = parameters;
    }

    public MoveOverDecorator(Effect effect, Map<String, String> parameters) {
        this(effect, Map.of(), parameters);
    }

    @Override
    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    @Override
    public boolean require(Worker worker) {
        // TODO: get the current worker. Check if in his range
        //       there are enemies' workers.
        //       If there are, then another condition needs to be checked:
        //          If swapSpace == true, then the effect is applicable.
        //          Otherwise it is necessary to check the backwards space
        //          (see Minotaur effect for more info).
        //       Otherwise effect is not applicable.

        worker.getPosition();

        if (Boolean.parseBoolean(requirements.get(XMLName.SWAP_SPACE))) {
            return true;
        } else if (Boolean.parseBoolean(requirements.get(XMLName.PUSH_BACK))) {

            return true;
        }

        return effect.require(worker);
    }

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
