package model.board;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SpaceTest {

    private Space space1;
    private Space space2;

    @Before
    public void setUp() throws Exception {
        space1 = new Space(new Position(0, 0));
        space2 = new Space(new Position(1, 2));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isBorderLine_True() {
        assertTrue(space1.isBorderLine());
    }

    @Test
    public void isBorderLine_False() {
        assertFalse(space2.isBorderLine());
    }

    @Test
    public void isFree_True() {
        assertTrue(space1.isFree());
    }

    @Test
    public void isFree_False() {
        assertFalse(space2.isFree());
    }
}