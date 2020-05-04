package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.SimpleEffect;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

public class GodTest {

    private God god;
    private ReducedGod reducedGod;
    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new SimpleEffect(EffectType.YOUR_BUILD);

        god = new God.Builder("Name Test")
                .withCaption("Caption Test")
                .withDescription("Description Test")
                .withEffects(List.of(effect))
                .build();
        reducedGod = new ReducedGod(god);
    }

    @After
    public void tearDown() throws Exception {
        god = null;
    }

    @Test
    public void getEffectByType_EffectFound_NotNull() {
        assertEquals(effect, god.getEffectByType(EffectType.YOUR_BUILD));
    }

    @Test
    public void getEffectByType_EffectNotFound_Null() {
        assertNull(god.getEffectByType(EffectType.YOUR_MOVE));
    }

    @Test
    public void equals_God() {
        God god2 = new God.Builder("Name Test").build();
        assertTrue(god.equals(god2) && god.equals(god2));
        assertTrue(god.hashCode() == god.hashCode());
    }

    @Test
    public void addObserverToAllEffects() throws IllegalAccessException {
        Observer obs = message -> {
        };
        god.addObserverToAllEffects(obs);
        Effect effect = god.getEffectByType(EffectType.YOUR_BUILD);
    }

    @Test
    public void getName_God_ReducedGod() {
        assertEquals(god.getName(), reducedGod.getName());
    }

    @Test
    public void getCaption_God_ReducedGod() {
        assertEquals(god.getCaption(), reducedGod.getCaption());
    }

    @Test
    public void getDescription_God_ReducedGod() {
        assertEquals(god.getDescription(), reducedGod.getDescription());
    }

    @Test
    public void equals_ReducedGod() {
        ReducedGod r2 = new ReducedGod(god);
        assertTrue(reducedGod.equals(r2) && reducedGod.equals(r2));
        assertTrue(reducedGod.hashCode() == r2.hashCode());
    }
}