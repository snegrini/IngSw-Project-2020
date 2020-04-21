package model.player;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.board.Space;
import model.effects.Effect;
import model.enumerations.Color;
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
        worker = new Worker(Color.BLUE, new Position(3, 4));
        reducedWorker = new ReducedWorker(worker);
    }

    @After
    public void tearDown() throws Exception {
        worker = null;
        reducedWorker = null;
    }

    @Test
    public void build() {
        Position position = new Position(0, 0);
        Board board = Game.getInstance().getBoard();
        Space space = board.getSpace(position);

        assertEquals(0, space.getLevel());
        worker.build(position);
        assertEquals(1, space.getLevel());
    }

    @Test
    public void move() {
        Position position = new Position(3, 4);
        worker.move(position);
        assertEquals(position, worker.getPosition());
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
    public void getPossibleMoves_correctInput_correctOutput() {
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
    public void positionGetterAndSetter() {
        Position position = new Position(4, 4);
        assertNotEquals(position, worker.getPosition());
        worker.setPosition(position);
        assertEquals(position, worker.getPosition());
    }

    @Test
    public void getMoveHistory() {

    }

    @Test
    public void getPosition_Worker_ReducedWorker() {
        assertEquals(worker.getPosition(), reducedWorker.getPosition());
    }

    @Test
    public void getColor_Worker_ReducedWorker() {
        assertEquals(worker.getColor(), reducedWorker.getColor());
    }
}