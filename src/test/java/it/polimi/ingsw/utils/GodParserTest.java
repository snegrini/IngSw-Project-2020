package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.God;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class GodParserTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void parseGods() {
        List<God> gods = GodParser.parseGods("gods.xml");
        assertTrue(gods.size() > 0);
    }
}