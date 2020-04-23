package model.effects;

import model.enumerations.EffectType;
import model.enumerations.TargetType;
import model.player.Worker;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

public class MoveAgainDecorator extends EffectDecorator {

    private Map<String, TargetType> targetTypeMap;
    private boolean goBack;
    private int quantity;

    public MoveAgainDecorator(Effect effect, Map<String, String> requirements,
                              Map<String, String> parameters) {
        this.effect = effect;
        this.requirements = requirements;
        this.parameters = parameters;
    }

    @Override
    public void apply(List<Worker> workers) {
        effect.apply(workers);
    }

    @Override
    public boolean require(Worker worker) {
        //
        return effect.require(worker);
    }

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
