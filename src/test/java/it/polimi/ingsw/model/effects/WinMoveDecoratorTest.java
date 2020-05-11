package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.MoveType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class WinMoveDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new WinMoveDecorator(new SimpleEffect(EffectType.YOUR_MOVE_AFTER), Map.of(),
                MoveType.DOWN, 2);
    }

    @After
    public void tearDown() throws Exception {
        effect = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_PlayerWin() {
    }

    @Test
    public void applyEffect_PlayerNotWin() {
    }
}