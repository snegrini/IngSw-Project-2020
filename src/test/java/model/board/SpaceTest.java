package model.board;

import static org.junit.Assert.*;

public class SpaceTest {

    private Space space;

    @org.junit.Before
    public void setUp() throws Exception {
        space = new Space(new Position(0, 0));
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void isBorderLine() {
    }

    @org.junit.Test
    public void isFree() {
    }
}