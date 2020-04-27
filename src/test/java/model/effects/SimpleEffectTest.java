package model.effects;

import model.enumerations.EffectType;
import model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SimpleEffectTest {

    private Effect simpleEffect;

    @Before
    public void setUp() throws Exception {
        simpleEffect = new SimpleEffect(EffectType.YOUR_MOVE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void apply_doNothing() {
        List<Worker> workers = List.of();
        assertEquals(List.of(), workers);
        simpleEffect.apply(List.of());
        assertEquals(List.of(), workers);
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