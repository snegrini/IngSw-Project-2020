package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveAgainDecoratorTest {

    private Effect effectCanGoBack;
    private Effect effectNoGoBack;

    @Before
    public void setUp() throws Exception {
        effectCanGoBack = new MoveAgainDecorator(new SimpleEffect(PhaseType.YOUR_MOVE_AFTER),
                1, true);
        effectNoGoBack = new MoveAgainDecorator(new SimpleEffect(PhaseType.YOUR_MOVE_AFTER),
                1, false);
    }

    @After
    public void tearDown() throws Exception {
        effectCanGoBack = null;
        effectNoGoBack = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_GoBack() {
        Board board = Game.getInstance().getBoard();
        Position origPosition = new Position(0, 1);
        Worker worker = new Worker(origPosition);
        Position movePosition = new Position(0, 0);

        // Simulate a last move.
        board.moveWorker(worker, movePosition);

        // Fill all the other neighbour spaces.
        board.getSpace(1, 0).setDome(true);
        board.getSpace(1, 1).setDome(true);

        assertTrue(effectCanGoBack.require(worker));
        effectCanGoBack.prepare(worker);
        effectCanGoBack.apply(worker, origPosition);
        assertEquals(worker, board.getSpace(origPosition).getWorker());
        assertNull(board.getSpace(movePosition).getWorker());
        effectCanGoBack.clear(worker);
    }

    @Test
    public void applyEffect_NoGoBack() {
        Board board = Game.getInstance().getBoard();
        Position origPosition = new Position(0, 1);
        Worker worker = new Worker(origPosition);
        Position movePosition = new Position(0, 0);

        // Simulate a last move.
        board.moveWorker(worker, movePosition);

        // Fill all the other neighbour spaces.
        board.getSpace(1, 0).setDome(true);
        board.getSpace(1, 1).setDome(true);

        assertFalse(effectNoGoBack.require(worker));
    }
}