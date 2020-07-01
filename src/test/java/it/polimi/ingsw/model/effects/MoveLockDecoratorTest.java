package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the {@link MoveLockDecorator} methods.
 */
public class MoveLockDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() {
        Map<String, String> reqs = Map.of(XMLName.USERCONFIRM.getText(), "false", XMLName.MOVE.getText(), "UP");
        effect = new MoveLockDecorator(new SimpleEffect(PhaseType.YOUR_MOVE_AFTER), reqs, MoveType.UP);
        effect.addTargetType(XMLName.PARAMETERS, TargetType.ALL_OPP_WORKERS);
    }

    @After
    public void tearDown() {
        effect = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_MovingUp() {
        Player p1 = new Player("mario");
        Player p2 = new Player("anna");

        Worker w1 = new Worker(Color.RED);
        Worker w2 = new Worker(Color.RED);
        p1.addWorker(w1);
        p1.addWorker(w2);
        p1.initWorkers(List.of(new Position(0, 0), new Position(2, 2)));

        Worker w3 = new Worker(Color.BLUE);
        Worker w4 = new Worker(Color.BLUE);
        p2.addWorker(w3);
        p2.addWorker(w4);
        p2.initWorkers(List.of(new Position(3, 3), new Position(1, 1)));

        Game game = Game.getInstance();
        Board board = game.getBoard();
        game.initWorkersOnBoard(List.of(w1, w2, w3, w4));
        game.addPlayer(p1);
        game.addPlayer(p2);

        Position movePosition = new Position(0, 1);

        // Build a block to simulate the move UP.
        board.getSpace(movePosition).increaseLevel(1);

        board.moveWorker(w1, movePosition);
        assertTrue(effect.require(w1));
        assertFalse(effect.isUserConfirmNeeded());
        effect.prepare(w1);
        effect.apply(w1, null);

        assertFalse(w1.checkLockedMovement(MoveType.UP));
        assertFalse(w2.checkLockedMovement(MoveType.UP));
        assertTrue(w3.checkLockedMovement(MoveType.UP));
        assertTrue(w4.checkLockedMovement(MoveType.UP));

        effect.clear(w1);

        assertTrue(w3.checkLockedMovement(MoveType.UP));
        assertTrue(w4.checkLockedMovement(MoveType.UP));

        effect.clear(w1);

        assertFalse(w3.checkLockedMovement(MoveType.UP));
        assertFalse(w4.checkLockedMovement(MoveType.UP));
    }

    @Test
    public void applyEffect_NotMovingUp() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position movePosition = new Position(0, 1);
        board.moveWorker(worker, movePosition);

        assertFalse(effect.require(worker));
    }
}