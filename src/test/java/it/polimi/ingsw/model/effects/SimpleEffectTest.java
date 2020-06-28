package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.model.enumerations.XMLName.PARAMETERS;
import static org.junit.Assert.*;

public class SimpleEffectTest {

    private Effect simpleEffect;

    @Before
    public void setUp() {
        simpleEffect = new SimpleEffect(PhaseType.YOUR_MOVE);
    }

    @After
    public void tearDown() {
        simpleEffect = null;
    }

    @Test
    public void test_SimpleEffect_DoNothing() {
        Worker worker = new Worker(Color.BLUE);

        assertTrue(simpleEffect.require(worker));
        simpleEffect.prepare(worker);
        simpleEffect.apply(worker, new Position(0, 0));
        simpleEffect.clear(worker);
        assertEquals(worker, worker);
        assertEquals(simpleEffect, simpleEffect);
    }

    @Test
    public void getSimpleEffect() {
        assertEquals(PhaseType.YOUR_MOVE, simpleEffect.getPhaseType());
    }

    @Test
    public void addAndGetTargetType() {
        assertNull(simpleEffect.getTargetType(PARAMETERS));
        simpleEffect.addTargetType(PARAMETERS, TargetType.YOUR_ACTIVE_WORKER);
        assertEquals(TargetType.YOUR_ACTIVE_WORKER, simpleEffect.getTargetType(PARAMETERS));
    }

    @Test
    public void isUserConfirmNeeded() {
        assertFalse(simpleEffect.isUserConfirmNeeded());
    }

}