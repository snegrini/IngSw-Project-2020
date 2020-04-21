package model.board;

import static org.junit.Assert.*;

import model.effects.Effect;
import model.enumerations.Color;
import model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SpaceTest {

    private Board board;
    private Space space;
    private ReducedSpace reducedSpace;

    @Before
    public void setUp() throws Exception {
        board = new Board();
        space = board.getSpace(new Position(0, 0));
    }

    @After
    public void tearDown() throws Exception {
        space = null;
    }

    @Test
    public void isFree_True() {
        assertTrue(space.isFree());
    }

    @Test
    public void isFree_noDome_True() {
        space.setDome(false);
        assertFalse(space.hasDome());
        assertTrue(space.isFree());
    }

    @Test
    public void isFree_setDome_False() {
        space.setDome(true);
        assertTrue(space.hasDome());
        assertFalse(space.isFree());
    }

    @Test
    public void isFree_setWorker_False() {
        Worker worker = new Worker(Color.BLUE, new Position(0, 0));
        space.setWorker(worker);
        assertEquals(worker, space.getWorker());
        assertFalse(space.isFree());
    }

    @Test
    public void increaseLevel() {
        assertEquals(0, space.getLevel());
        assertTrue(space.increaseLevel(2));
        assertEquals(2, space.getLevel());
    }

    @Test
    public void increaseLevel_MaxLevelLimit() {
        assertEquals(0, space.getLevel());
        assertFalse(space.increaseLevel(Space.MAX_LEVEL + 1));
        assertEquals(0, space.getLevel());
    }

    @Test
    public void increaseLevel_NegativeInput() {
        assertEquals(0, space.getLevel());
        assertFalse(space.increaseLevel(-1));
        assertEquals(0, space.getLevel());
    }

    @Test
    public void decreaseLevel() {
        assertTrue(space.increaseLevel(1));
        assertEquals(1, space.getLevel());
        assertTrue(space.decreaseLevel(1));
        assertEquals(0, space.getLevel());
    }

    @Test
    public void decreaseLevel_MinLevelLimit() {
        assertEquals(0, space.getLevel());
        assertFalse(space.decreaseLevel(1));
        assertEquals(0, space.getLevel());
    }

    @Test
    public void decreaseLevel_NegativeInput() {
        assertEquals(0, space.getLevel());
        assertFalse(space.decreaseLevel(-1));
        assertEquals(0, space.getLevel());
    }

    @Test
    public void getLevel_Space_ReducedSpace() {
        reducedSpace = new ReducedSpace(space);
        assertEquals(space.getLevel(), reducedSpace.getLevel());
    }

    @Test
    public void getWorker_Space_ReducedSpace() {
        Worker worker = new Worker(Color.BLUE, new Position(0, 0));
        space.setWorker(worker);

        reducedSpace = new ReducedSpace(space);
        assertEquals(space.getWorker().getColor(), reducedSpace.getReducedWorker().getColor());
        assertEquals(space.getWorker().getPosition(), reducedSpace.getReducedWorker().getPosition());
    }

    @Test
    public void hasDome_Space_ReducedSpace() {
        reducedSpace = new ReducedSpace(space);
        assertEquals(space.hasDome(), reducedSpace.hasDome());
    }
}