package model.board;

import static org.junit.Assert.*;

import model.effects.Effect;
import model.effects.SimpleEffect;
import model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SpaceTest {

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
    public void isFree_True() {
        assertTrue(space.isFree());
    }

    @Test
    public void isFree_noDome_True() {
        space.setDome(false);
        assertTrue(space.isFree());
    }

    @Test
    public void isFree_setDome_False() {
        space.setDome(true);
        assertFalse(space.isFree());
    }

    @Test
    public void isFree_setWorker_False() {
        space.setWorker(new Worker(new Position(0, 0), new ArrayList<Effect>()));
        assertFalse(space.isFree());
    }
}