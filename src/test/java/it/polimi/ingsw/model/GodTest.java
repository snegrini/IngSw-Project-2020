package it.polimi.ingsw.model;

import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.SimpleEffect;
import it.polimi.ingsw.model.enumerations.PhaseType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GodTest {

    private God god;
    private ReducedGod reducedGod;
    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new SimpleEffect(PhaseType.YOUR_BUILD);

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
        assertEquals(effect, god.getEffectByType(PhaseType.YOUR_BUILD));
    }

    @Test
    public void getEffectByType_EffectNotFound_Null() {
        assertNull(god.getEffectByType(PhaseType.YOUR_MOVE));
    }

    @Test
    public void equals_God() {
        God god2 = new God.Builder("Name Test").build();
        assertEquals(god, god2);
        assertEquals(god.hashCode(), god2.hashCode());
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
        assertEquals(reducedGod, r2);
        assertEquals(reducedGod.hashCode(), r2.hashCode());
    }
}