package it.polimi.ingsw.model.effects;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.player.Worker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class BuildAgainDecoratorTest {

    private Effect effect1;
    private Effect effect2;

    @Before
    public void setUp() throws Exception {
        effect1 = new BuildAgainDecorator(new SimpleEffect(EffectType.YOUR_BUILD), Map.of(), 1,
                false, false, false);
        effect2 = new BuildAgainDecorator(new SimpleEffect(EffectType.YOUR_BUILD), Map.of(), 1,
                false, false, true);
    }

    @After
    public void tearDown() throws Exception {
        effect1 = null;
        effect2 = null;
        Game.resetInstance();
    }

    @Test
    public void getEffectType() {
        assertEquals(EffectType.YOUR_BUILD, effect1.getEffectType());
    }

    @Test
    public void applyEffect1_BuildNotPossible() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position buildPosition = new Position(0, 1);

        // Simulate a last build.
        board.buildBlock(worker, buildPosition);

        // Fill all the other neighbour spaces.
        board.getSpace(1, 0).setDome(true);
        board.getSpace(1, 1).setDome(true);

        assertFalse(effect1.require(worker));
        effect1.prepare(worker);
        effect1.apply(worker, buildPosition);
        assertEquals(1, board.getSpace(buildPosition).getLevel());
        effect1.clear(worker);
    }

    @Test
    public void applyEffect1_BuildPossible() {
        Worker worker = new Worker(new Position(0, 0));
        Position buildAgainPosition = new Position(0, 1);

        assertTrue(effect1.require(worker));
        effect1.prepare(worker);
        effect1.apply(worker, buildAgainPosition);
        assertEquals(1, Game.getInstance().getBoard().getSpace(buildAgainPosition).getLevel());
        effect1.clear(worker);
    }

    @Test
    public void applyEffect2_BuildNotPossible() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position buildAgainPosition = new Position(0, 1);

        // Simulate a last build.
        board.buildBlock(worker, buildAgainPosition);

        // Set a dome on the last build position.
        board.getSpace(buildAgainPosition).setDome(true);

        assertFalse(effect2.require(worker));
    }

    @Test
    public void applyEffect2_BuildPossible() {
        Board board = Game.getInstance().getBoard();
        Worker worker = new Worker(new Position(0, 0));
        Position buildAgainPosition = new Position(0, 1);

        // Simulate a last build.
        board.buildBlock(worker, buildAgainPosition);

        assertTrue(effect2.require(worker));
        effect2.prepare(worker);
        effect2.apply(worker, buildAgainPosition);
        assertEquals(2, Game.getInstance().getBoard().getSpace(buildAgainPosition).getLevel());
        effect2.clear(worker);
    }


}