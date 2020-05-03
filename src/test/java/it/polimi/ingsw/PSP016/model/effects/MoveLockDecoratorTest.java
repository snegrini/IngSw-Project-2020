package it.polimi.ingsw.PSP016.model.effects;

import it.polimi.ingsw.PSP016.model.enumerations.EffectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveLockDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new SimpleEffect(EffectType.YOUR_MOVE);
    }

    @After
    public void tearDown() throws Exception {
        effect = null;
    }

    @Test
    public void apply() {

    }

    @Test
    public void require() {

    }

    @Test
    public void getEffectType() {
    }

    @Test
    public void setEffectType() {
    }

    @Test
    public void getTargetType() {
    }

    @Test
    public void addTargetType() {
    }
}