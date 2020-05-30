package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkerTest {

    private Worker worker;
    private ReducedWorker reducedWorker;

    @Before
    public void setUp() throws Exception {
        worker = new Worker(new Position(3, 4));
        worker.setColor(Color.BLUE);
        reducedWorker = new ReducedWorker(worker);
    }

    @After
    public void tearDown() throws Exception {
        worker = null;
        reducedWorker = null;
        Game.getInstance().resetInstance();
    }

    @Test
    public void move_moveHistory() {
        Position oldPosition = worker.getPosition();
        Position newPosition = new Position(3, 3);
        worker.move(newPosition);
        assertEquals(newPosition, worker.getPosition());
        assertEquals(oldPosition, worker.getHistory().getMovePosition());
    }

    @Test
    public void getPossibleBuilds() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(4, 4).setDome(true);

        List<Position> expectedBuildPositions = new ArrayList<>();
        expectedBuildPositions.add((new Position(2, 3)));
        expectedBuildPositions.add((new Position(2, 4)));
        expectedBuildPositions.add((new Position(3, 3)));
        expectedBuildPositions.add((new Position(4, 3)));

        assertEquals(expectedBuildPositions, worker.getPossibleBuilds());
    }

    @Test
    public void getPossibleMoves_levelIncompatible() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(new Position(3, 3)).increaseLevel(2);

        List<Position> listPositionResult = new ArrayList<>();
        listPositionResult.add(new Position(2, 3));
        listPositionResult.add(new Position(2, 4));
        listPositionResult.add(new Position(4, 3));
        listPositionResult.add(new Position(4, 4));
        // (3,3) is not in the list of possible moves because of incompatible levels.

        assertEquals(listPositionResult, worker.getPossibleMoves());
    }

    @Test
    public void getPossibleMoves_spaceNotFree() {
        Board board = Game.getInstance().getBoard();
        Worker w2 = new Worker(new Position(4, 4));
        w2.setColor(Color.BLUE);
        board.getSpace(4, 4).setWorker(w2);

        List<Position> listPositionResult = new ArrayList<>();
        listPositionResult.add(new Position(2, 3));
        listPositionResult.add(new Position(2, 4));
        listPositionResult.add(new Position(3, 3));
        listPositionResult.add(new Position(4, 3));
        // (4,4) is not in the list of possible moves because it is not free.

        assertEquals(listPositionResult, worker.getPossibleMoves());
    }

    @Test
    public void getPossibleMoves_lockedMovements() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(4, 4).increaseLevel(1);

        // Adds a lock movement to the worker
        worker.addLockedMovement(MoveType.UP);

        List<Position> listPositionResult = new ArrayList<>();
        listPositionResult.add(new Position(2, 3));
        listPositionResult.add(new Position(2, 4));
        listPositionResult.add(new Position(3, 3));
        listPositionResult.add(new Position(4, 3));
        // (4,4) is not in the list of possible moves because the player has the MoveType UP locked.

        assertEquals(listPositionResult, worker.getPossibleMoves());
    }

    @Test
    public void positionInitAndGetter() {
        Position position = new Position(4, 4);
        assertNotEquals(position, worker.getPosition());
        worker.initPosition(position);
        assertEquals(position, worker.getPosition());
    }

    @Test
    public void updateBuildHistory() {
        Position position = new Position(0, 0);

        worker.updateBuildHistory(position);
        assertEquals(position, worker.getHistory().getBuildPosition());
    }


    @Test
    public void hasMovedUp() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(3, 3).increaseLevel(1);
        worker.move(new Position(3, 3));

        assertTrue(worker.hasMovedUp());
    }

    @Test
    public void hasMovedDown() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(3, 4).increaseLevel(1);

        worker.move(new Position(2, 3));

        assertTrue(worker.hasMovedDown());
    }

    @Test
    public void hasMovedFlat() {
        worker.move(new Position(3, 3));
        assertTrue(worker.hasMovedFlat());
    }

    @Test
    public void add_remove_check_lockedMovement() {
        assertFalse(worker.checkLockedMovement(MoveType.UP));
        worker.addLockedMovement(MoveType.UP);
        assertTrue(worker.checkLockedMovement(MoveType.UP));
        worker.removeLockedMovement(MoveType.UP);
        assertFalse(worker.checkLockedMovement(MoveType.UP));
    }

    @Test
    public void removeAllLockedMovement() {
        worker.addLockedMovement(MoveType.UP);
        worker.addLockedMovement(MoveType.DOWN);
        worker.removeAllLockedMovements();
        assertFalse(worker.checkLockedMovement(MoveType.UP));
        assertFalse(worker.checkLockedMovement(MoveType.DOWN));
    }

    @Test
    public void setColor() {
        worker.setColor(Color.BLUE);
        assertEquals(Color.BLUE, worker.getColor());
    }

    @Test
    public void getPosition_Worker_ReducedWorker() {
        assertEquals(worker.getPosition(), reducedWorker.getPosition());
    }

    @Test
    public void getColor_Worker_ReducedWorker() {
        assertEquals(worker.getColor(), reducedWorker.getColor());
    }

    @Test
    public void filterLockedMovementPositions() {
        Board board = Game.getInstance().getBoard();
        board.getSpace(4, 4).increaseLevel(1);

        // Adds a lock movement to the worker
        worker.addLockedMovement(MoveType.UP);

        List<Position> positionList = new ArrayList<>();
        positionList.add(new Position(2, 3));
        positionList.add(new Position(4, 4));

        List<Position> positionListResult = new ArrayList<>();
        positionListResult.add(new Position(2, 3));

        // (4,4) should is removed from the list because the worker has the MoveType UP locked.
        assertEquals(positionListResult, worker.filterLockedMovementPositions(positionList));
    }
}