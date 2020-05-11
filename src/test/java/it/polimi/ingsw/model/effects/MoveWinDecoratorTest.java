package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class MoveWinDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new MoveWinDecorator(new SimpleEffect(PhaseType.YOUR_MOVE_AFTER), Map.of(),
                MoveType.DOWN, 2);

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
    }

    @After
    public void tearDown() throws Exception {
        effect = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_PlayerWin() {
        Board board = Game.getInstance().getBoard();
        Position pos = new Position(0, 0);

        // Set the worker space on a level greater than 2.
        board.getSpace(pos).increaseLevel(2);
        Worker worker = board.getWorkerByPosition(new Position(0, 0));
        board.moveWorker(worker, new Position(1, 0));

        assertTrue(effect.require(worker));
        effect.prepare(worker);
        effect.apply(worker, new Position(1, 0));
        // TODO check game state
    }

    @Test
    public void applyEffect_PlayerNotWin() {
    }
}