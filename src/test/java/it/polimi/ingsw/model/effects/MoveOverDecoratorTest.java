package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This class tests the {@link MoveOverDecorator} methods.
 */
public class MoveOverDecoratorTest {

    private Effect effectSwapSpace;
    private Effect effectPushBack;

    private Game game;

    @Before
    public void setUp() {
        effectSwapSpace = new MoveOverDecorator(new SimpleEffect(PhaseType.YOUR_MOVE), Map.of(),
                false, true);
        effectPushBack = new MoveOverDecorator(new SimpleEffect(PhaseType.YOUR_MOVE), Map.of(),
                true, false);

        this.game = Game.getInstance();

        Player p1 = new Player("mario");
        Player p2 = new Player("anna");

        Worker w1 = new Worker(Color.RED);
        Worker w2 = new Worker(Color.RED);
        p1.addWorker(w1);
        p1.addWorker(w2);
        p1.initWorkers(List.of(new Position(0, 0), new Position(4, 4)));

        Worker w3 = new Worker(Color.BLUE);
        Worker w4 = new Worker(Color.BLUE);
        p2.addWorker(w3);
        p2.addWorker(w4);
        p2.initWorkers(List.of(new Position(0, 1), new Position(2, 2)));

        this.game.initWorkersOnBoard(List.of(w1, w2, w3, w4));
        this.game.addPlayer(p1);
        this.game.addPlayer(p2);
    }

    @After
    public void tearDown() {
        effectSwapSpace = null;
        effectPushBack = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_SwapSpace_Swap() {
        Board board = game.getBoard();
        Worker w1 = game.getWorkerByPosition(new Position(0, 0));
        Worker w3 = game.getWorkerByPosition(new Position(0, 1));
        Position movePosition = new Position(0, 1);

        assertTrue(effectSwapSpace.require(w1));

        effectSwapSpace.prepare(w1);
        effectSwapSpace.apply(w1, movePosition);
        effectSwapSpace.clear(w1);

        assertEquals(w1, board.getSpace(movePosition).getWorker());
        assertEquals(w3, board.getSpace(w1.getHistory().getMovePosition()).getWorker());
    }

    @Test
    public void requireEffect_SwapSpace_NormalMove() {
        Position orig = new Position(4, 4);
        Worker w2 = game.getWorkerByPosition(orig);

        assertFalse(effectSwapSpace.require(w2));
    }

    @Test
    public void applyEffect_PushBack_Push() {
        Board board = game.getBoard();
        Worker w1 = game.getWorkerByPosition(new Position(0, 0));
        Worker w3 = game.getWorkerByPosition(new Position(0, 1));
        Position movePosition = new Position(0, 1);
        Space nextSpace = board.getNextSpaceInLine(w1.getPosition(), w3.getPosition());

        assertTrue(effectPushBack.require(w1));

        effectPushBack.prepare(w1);
        effectPushBack.apply(w1, movePosition);
        effectPushBack.clear(w1);

        assertEquals(w1, board.getSpace(movePosition).getWorker());
        assertEquals(w3, nextSpace.getWorker());
    }

    @Test
    public void applyEffect_PushBack_NormalMove() {
        Position orig = new Position(4, 4);
        Worker w2 = game.getWorkerByPosition(orig);

        assertFalse(effectPushBack.require(w2));
    }

    @Test
    public void applyEffect_BadMove() {
        Board board = game.getBoard();
        Position orig = new Position(0, 0);
        Position movePosition = new Position(3, 3);
        Worker w2 = game.getWorkerByPosition(orig);

        assertTrue(effectSwapSpace.require(w2));
        effectSwapSpace.prepare(w2);
        effectSwapSpace.apply(w2, movePosition);

        // Worker will not be moved
        assertEquals(w2, board.getSpace(orig).getWorker());
        assertNotEquals(w2, board.getSpace(movePosition).getWorker());
    }
}