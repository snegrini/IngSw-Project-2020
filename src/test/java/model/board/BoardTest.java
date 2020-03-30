package model.board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;
    private Space space;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        space = board.getSpace(new Position(0, 0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSpace() {
        assertNotNull(board.getSpace(new Position(0, 0)));
        assertEquals(space, board.getSpace(new Position(0, 0)));
    }
}