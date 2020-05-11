package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BuildDomeDecoratorTest {

    private Effect effect;

    @Before
    public void setUp() throws Exception {
        effect = new BuildDomeDecorator(new SimpleEffect(PhaseType.YOUR_BUILD), Map.of());
    }

    @After
    public void tearDown() throws Exception {
        effect = null;
        Game.resetInstance();
    }

    @Test
    public void applyEffect_BuildNotPossible() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position buildPosition = new Position(0, 1);

        // Simulate a last build.
        board.buildDomeForced(worker, buildPosition);

        // Fill all the neighbour spaces.
        board.getSpace(buildPosition).setDome(true);
        board.getSpace(1, 1).setDome(true);
        board.getSpace(1, 0).setDome(true);

        assertFalse(effect.require(worker));
    }

    @Test
    public void applyEffect_BuildPossible() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position lastBuildPosition = new Position(0, 1);
        Position buildAgainPosition = new Position(1, 1);

        // Simulate a last build.
        board.buildDomeForced(worker, lastBuildPosition);

        assertTrue(effect.require(worker));

        effect.prepare(worker);
        effect.apply(worker, buildAgainPosition);

        assertEquals(0, board.getSpace(buildAgainPosition).getLevel());
        assertTrue(board.getSpace(buildAgainPosition).hasDome());

        effect.clear(worker);
    }
}