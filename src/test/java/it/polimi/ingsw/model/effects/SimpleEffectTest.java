package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.EffectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleEffectTest {

    private Effect simpleEffect;

    @Before
    public void setUp() throws Exception {
        simpleEffect = new SimpleEffect(EffectType.YOUR_MOVE);
    }

    @After
    public void tearDown() throws Exception {
        simpleEffect = null;
    }

    @Test
    public void apply_doNothing() {
        simpleEffect.apply(null, null);
    }

    @Test
    public void require_True() {
        assertTrue(simpleEffect.require(null));
    }

    @Test
    public void getSimpleEffect() {
        assertEquals(EffectType.YOUR_MOVE, simpleEffect.getEffectType());
    }
}