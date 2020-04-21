package model;

import model.effects.Effect;
import model.effects.SimpleEffect;
import model.enumerations.EffectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}