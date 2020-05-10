package it.polimi.ingsw.parser;

import it.polimi.ingsw.model.God;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class GodParserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseGods() {
        List<God> gods = GodParser.parseGods();
        assertTrue(gods.size() > 0);
    }
}