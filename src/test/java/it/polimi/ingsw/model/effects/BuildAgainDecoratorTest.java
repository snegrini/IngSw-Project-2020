package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.enumerations.EffectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BuildAgainDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new BuildAgainDecorator(new SimpleEffect(EffectType.YOUR_BUILD), Map.of(), 1,
                false, false, false);
    }

    @After
    public void tearDown() throws Exception {
        effect = null;
    }

    @Test
    public void apply() {
    }

    @Test
    public void prepare() {
    }

    @Test
    public void clear() {
    }

    @Test
    public void require() {
    }
}