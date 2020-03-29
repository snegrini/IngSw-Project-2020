package model.effects;

public class WinDownDecorator extends EffectDecorator {

    private int numOfLevel;

    public WinDownDecorator(Effect effect, int numOfLevel) {
        this.effect = effect;
        this.numOfLevel = numOfLevel;
    }

    public void apply(){
        // TODO
    }

    public boolean require(){
        // TODO
    }

    public int getNumOfLevel() {
        return numOfLevel;
    }
}
