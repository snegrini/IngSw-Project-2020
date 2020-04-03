package model.effects;

import model.player.Worker;

import java.util.List;

public class MoveAgainDecorator extends EffectDecorator {

    private int numOfMoves;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int numOfMoves, boolean goBack) {
        this.effect = effect;
        this.numOfMoves = numOfMoves;
        this.goBack = goBack;
    }

    public void apply(List<Worker> targetWorkers) {
        // TODO
    }

    public boolean require(Worker worker) {
        // TODO

        return effect.require(worker);
    }

    public int getNumOfMoves() {
        return numOfMoves;
    }

    public boolean isGoBack() {
        return goBack;
    }
}
