package parser;

import model.God;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GodParserTest {

    private List<God> gods;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseGods() {
        gods = GodParser.parseGods();
        assertTrue(gods.size() > 0);
    }
}