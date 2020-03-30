package model.player;

import model.Game;
import model.board.Position;
import model.effects.Effect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkerTest {

    private Worker worker;

    @Before
    public void setUp() throws Exception {

        worker = new Worker(new Position(3, 4), new ArrayList<Effect>());
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void move_correctInput_correctOutput() {


        worker.move(new Position(3, 3));
        assertEquals(new Position(3, 3), worker.getPosition());
    }

    @Test
    public void getPossibleMoves_correctInput_correctOutput() {
        List<Position> listPositions = new ArrayList<Position>();
        listPositions = worker.getPossibleBuilds();

        List<Position> listPositionResult = new ArrayList<Position>();
        listPositionResult.add((new Position(2, 3)));
        listPositionResult.add((new Position(2, 4)));
        listPositionResult.add((new Position(4, 4)));
        listPositionResult.add((new Position(4, 3)));
        listPositionResult.add((new Position(3, 3)));

        assertTrue(listPositionResult.equals(listPositions));
    }
}