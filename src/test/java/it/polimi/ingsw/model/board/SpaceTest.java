package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This class tests the {@link Space} methods.
 */
public class SpaceTest {

    private Space space;
    private ReducedSpace reducedSpace;

    @Before
    public void setUp() {
        Board board = new Board();
        space = board.getSpace(new Position(0, 0));
    }

    @After
    public void tearDown() {
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
        Worker worker = new Worker(new Position(0, 0));
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
    public void removeWorker_WorkerPresent() {
        Worker worker = new Worker(new Position(0, 0));
        space.setWorker(worker);
        assertEquals(worker, space.getWorker());

        assertFalse(space.isFree());
        space.removeWorker();
        assertTrue(space.isFree());
    }

    @Test
    public void removeWorker_WorkerNotPresent() {
        assertTrue(space.isFree());
        space.removeWorker();
        assertTrue(space.isFree());
    }

    @Test
    public void getLevel_Space_ReducedSpace() {
        reducedSpace = new ReducedSpace(space);
        assertEquals(space.getLevel(), reducedSpace.getLevel());
    }

    @Test
    public void toString_ReducedSpace() {
        reducedSpace = new ReducedSpace(space);
        String expectedStr = "ReducedSpace{level=0, dome=false, reducedWorker=null}";
        assertEquals(expectedStr, reducedSpace.toString());
    }

    @Test
    public void getWorker_Space_ReducedSpace() {
        Worker worker = new Worker(new Position(0, 0));
        space.setWorker(worker);

        reducedSpace = new ReducedSpace(space);
        Assert.assertEquals(space.getWorker().getColor(), reducedSpace.getReducedWorker().getColor());
        Assert.assertEquals(space.getWorker().getPosition(), reducedSpace.getReducedWorker().getPosition());
    }

    @Test
    public void hasDome_Space_ReducedSpace() {
        reducedSpace = new ReducedSpace(space);
        assertEquals(space.hasDome(), reducedSpace.hasDome());
    }
}