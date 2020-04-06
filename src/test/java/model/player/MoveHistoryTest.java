package model.player;

import model.board.Position;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveHistoryTest {

    private MoveHistory moveHistory;

    @Before
    public void setUp() throws Exception {
        Position position = new Position(2,3);
        moveHistory = new MoveHistory(position, 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hasMovedUp() {
        Position movedUpPosition = new Position(3,3);
        assertTrue(moveHistory.hasMovedUp(movedUpPosition, 3));
    }

    @Test
    public void hasMovedDown() {
        Position movedDownPosition = new Position(3,3);
        assertTrue(moveHistory.hasMovedDown(movedDownPosition, 1));
    }

    @Test
    public void hasMovedFlat() {
        Position movedFlatPosition = new Position(3,3);
        assertTrue(moveHistory.hasMovedFlat(movedFlatPosition, 2));
    }

    @Test
    public void getLastPosition() {
        Position lastPosition = new Position(2,3);
        assertEquals(lastPosition, moveHistory.getLastPosition());
    }

    @Test
    public void setLastPosition() {

        Position lastPosition = new Position(1,3);
        moveHistory.setLastPosition(lastPosition);
        assertEquals(lastPosition, moveHistory.getLastPosition());
    }

    @Test
    public void getLastLevel() {

        assertEquals(2, moveHistory.getLastLevel());
    }

    @Test
    public void setLastLevel() {
        moveHistory.setLastLevel(3);
        assertEquals(3, moveHistory.getLastLevel());

    }
}