package model.board;

import model.enumerations.Color;
import model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void getNextSpaceInLine() {
        Position orig = new Position(2, 2);
        Position dest = new Position(1, 1);
        Space target = board.getNextSpaceInLine(orig, dest);

        assertEquals(board.getSpace(0, 0), target);
    }

    @Test
    public void getFreePositions_AllFree() {
        List<Position> expectedPositions = new ArrayList<>();
        for (int i = 0; i < Board.MAX_ROWS; i++) {
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                expectedPositions.add(new Position(i, j));
            }
        }

        assertEquals(expectedPositions, board.getFreePositions());
    }

    @Test
    public void getFreePositions_PositionOccupied() {
        List<Position> expectedPositions = new ArrayList<>();
        for (int i = 0; i < Board.MAX_ROWS; i++) {
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                expectedPositions.add(new Position(i, j));
            }
        }
        expectedPositions.remove(0);
        board.getSpace(0, 0).setDome(true);

        assertEquals(expectedPositions, board.getFreePositions());
    }

    @Test
    public void arePositionsFree_AllFree() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(4, 3));

        assertTrue(board.arePositionsFree(positions));
    }

    @Test
    public void arePositionsFree_PositionOccupied() {
        // Occupy a position
        board.getSpace(4, 3).setDome(true);

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(4, 3));

        assertFalse(board.arePositionsFree(positions));
    }

    @Test
    public void getNeighbours() {
        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(3, 3));
        expectedPositions.add(new Position(3, 4));
        expectedPositions.add(new Position(4, 3));

        assertEquals(expectedPositions, board.getNeighbours(new Position(4, 4)));
    }

    @Test
    public void getNeighbourWorkers_NoneFound() {
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.RED);

        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(3, 4).setWorker(w2);

        assertEquals(List.of(), board.getNeighbourWorkers(w1.getPosition()));
    }

    @Test
    public void getNeighbourWorkers_OnePresent() {
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(0, 1));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.RED);

        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(0, 1).setWorker(w2);

        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(0, 1));

        assertEquals(expectedPositions, board.getNeighbourWorkers(w1.getPosition()));
    }
}