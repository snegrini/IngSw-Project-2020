package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Worker;
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
        board = null;
        space = null;
        Game.resetInstance();
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
    public void getNextPositionInLine() {
        Position orig = new Position(2, 2);
        Position dest = new Position(1, 1);
        Position target = board.getNextPositionInLine(orig, dest);

        assertEquals(new Position(0, 0), target);
    }

    @Test
    public void initWorkers() {
        board.initWorkers(List.of()); // TODO
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

        assertEquals(List.of(), board.getNeighbourWorkers(w1.getPosition(), false));
    }

    @Test
    public void getNeighbourWorkers_OneOpponentPresent() {
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(0, 1));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.RED);

        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(0, 1).setWorker(w2);

        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(0, 1));

        assertEquals(expectedPositions, board.getNeighbourWorkers(w1.getPosition(), true));
    }

    @Test
    public void getNeighbourWorkers_FriendlyWorker() {
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(0, 1));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.BLUE);

        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(0, 1).setWorker(w2);

        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(0, 1));

        assertEquals(expectedPositions, board.getNeighbourWorkers(w1.getPosition(), false));
    }

    @Test
    public void isMovingBack_True() {
        Position orig = new Position(0, 0);
        Position dest = new Position(1, 0);
        Worker w1 = new Worker(Color.BLUE);
        w1.initPosition(orig);
        w1.move(dest);

        assertTrue(board.isMovingBack(w1, orig));
    }

    @Test
    public void isMovingBack_False() {
        Position orig = new Position(0, 0);
        Position dest = new Position(1, 0);
        Worker w1 = new Worker(Color.BLUE);
        w1.initPosition(orig);
        w1.move(dest);

        assertFalse(board.isMovingBack(w1, new Position(1, 1)));
    }

    @Test
    public void isMovingBack_NotNeighbours() {
        Position orig = new Position(0, 0);
        Position dest = new Position(4, 0);
        Worker w1 = new Worker(Color.BLUE);
        w1.initPosition(orig);
        w1.move(dest);

        assertFalse(board.isMovingBack(w1, orig));
    }

    @Test
    public void moveWorker() {
        Position orig = new Position(0, 0);
        Position dest = new Position(1, 0);
        Worker w1 = new Worker(orig);

        board.moveWorker(w1, dest);

        assertEquals(w1, board.getSpace(dest).getWorker());
        assertNull(board.getSpace(orig).getWorker());
    }

    @Test
    public void buildBlock_workerBuildHistory() {
        Position orig = new Position(0, 0);
        Position buildPos = new Position(1, 0);
        Worker w1 = new Worker(orig);

        board.buildBlock(w1, buildPos);
        w1.updateBuildHistory(buildPos);

        assertEquals(1, board.getSpace(buildPos).getLevel());
        assertEquals(buildPos, w1.getHistory().getBuildPosition());

        // Stack blocks to build a dome
        board.buildBlock(w1, buildPos);
        w1.updateBuildHistory(buildPos);
        board.buildBlock(w1, buildPos);
        w1.updateBuildHistory(buildPos);

        assertFalse(board.getSpace(buildPos).hasDome());

        // Build a dome
        board.buildBlock(w1, buildPos);
        w1.updateBuildHistory(buildPos);

        assertTrue(board.getSpace(buildPos).hasDome());
    }

    @Test
    public void resetAllLevels() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 2);
        board.getSpace(p1).increaseLevel(2);
        board.getSpace(p1).setDome(true);
        board.getSpace(p2).increaseLevel(1);

        board.resetAllLevels();

        ReducedSpace[][] reducedSpaces = board.getReducedSpaceBoard();

        for (int i = 0; i < Board.MAX_ROWS; i++) {
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                assertEquals(0, reducedSpaces[i][j].getLevel());
                assertFalse(reducedSpaces[i][j].hasDome());
            }
        }
    }

    @Test
    public void getMoveTypeByLevel_PositionsNotNeighbours() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 2);
        assertNull(board.getMoveTypeByLevel(p1, p2));
    }


    @Test
    public void getMoveTypeByLevel_PositionNotChanged() {
        Position p1 = new Position(0, 0);
        assertNull(board.getMoveTypeByLevel(p1, p1));
    }

    @Test
    public void getMoveTypeByLevel_LevelUp() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);
        board.getSpace(p2).increaseLevel(1);

        assertEquals(MoveType.UP, board.getMoveTypeByLevel(p1, p2));
    }

    @Test
    public void getMoveTypeByLevel_LevelDown() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);
        board.getSpace(p1).increaseLevel(1);

        assertEquals(MoveType.DOWN, board.getMoveTypeByLevel(p1, p2));
    }

    @Test
    public void getMoveTypeByLevel_SameLevel() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);

        assertEquals(MoveType.FLAT, board.getMoveTypeByLevel(p1, p2));
    }

    @Test
    public void swapWorkers() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(1, 0);
        Worker w1 = new Worker(p1);
        Worker w2 = new Worker(p2);

        board.swapWorkers(w1, w2);

        assertEquals(w1, board.getSpace(p2).getWorker());
        assertEquals(w2, board.getSpace(p1).getWorker());
    }

}