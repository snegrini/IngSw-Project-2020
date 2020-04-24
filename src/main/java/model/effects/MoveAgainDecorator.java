package model.effects;

import model.board.Position;
import model.enumerations.EffectType;
import model.enumerations.TargetType;
import model.player.Worker;
import network.message.Message;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoveAgainDecorator extends EffectDecorator {

    private int quantity;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int quantity, boolean goBack) {
        this.effect = effect;
        this.quantity = quantity;
        this.goBack = goBack;
    }

    @Override
    public void apply(List<Worker> workers) {
        effect.apply(workers);

    }

    @Override
    public boolean require(Worker worker) {
        List<Position> possibleMoves = worker.getPossibleMoves();

        if (!goBack) {
            Position lastPosition = worker.getMoveHistory().getLastPosition();
            possibleMoves = possibleMoves.stream()
                    .filter(pos -> !pos.equals(lastPosition))
                    .collect(Collectors.toList());
        }

        // TODO notifico la view del cambiamento
        //      oppure aggiungo il controller come observer di questa classe (cioè degli effect)
        //      e dal controller seleziono la vista con le varie mosse disponibili.
        //notifyObserver();

        return effect.require(worker);
    }

    @Override
    public EffectType getEffectType() {
        return effect.getEffectType();
    }
}
