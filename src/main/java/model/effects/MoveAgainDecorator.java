package model.effects;

public class MoveAgainDecorator extends EffectDecorator {

    private int numOfMoves;
    private boolean goBack;

    public MoveAgainDecorator(Effect effect, int numOfMoves, boolean goBack) {
        this.effect = effect;
        this.numOfMoves = numOfMoves;
        this.goBack = goBack;
    }

    public void apply() {
        // TODO
    }

    public boolean require() {
        // TODO

        return effect.require();
    }

    public int getNumOfMoves() {
        return numOfMoves;
    }

    public boolean isGoBack() {
        return goBack;
    }
}
