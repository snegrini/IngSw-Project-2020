package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.TargetType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.model.enumerations.XMLName.PARAMETERS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void clear_doNothing() {
        simpleEffect.clear(null);
    }

    @Test
    public void getSimpleEffect() {
        assertEquals(EffectType.YOUR_MOVE, simpleEffect.getEffectType());
    }

    @Test
    public void addAndGetTargetType() {
        assertEquals(null, simpleEffect.getTargetType(PARAMETERS));
        simpleEffect.addTargetType(PARAMETERS, TargetType.YOUR_ACTIVE_WORKER);
        assertEquals(TargetType.YOUR_ACTIVE_WORKER, simpleEffect.getTargetType(PARAMETERS));
    }
}