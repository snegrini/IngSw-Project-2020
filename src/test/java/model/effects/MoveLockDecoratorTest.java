package model.effects;

import model.enumerations.EffectType;
import model.enumerations.MoveType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

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