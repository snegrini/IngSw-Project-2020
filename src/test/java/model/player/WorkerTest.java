package model.player;

import model.board.Position;
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

    @Before
    public void setUp() throws Exception {
        worker = new Worker(Color.BLUE, new Position(3, 4));
    }

    @After
    public void tearDown() throws Exception {
        worker = null;
    }


    @Test
    public void move_correctInput_correctOutput() {
        worker.move(new Position(3, 3));
        assertEquals(new Position(3, 3), worker.getPosition());
    }

    @Test
    public void getPossibleMoves_correctInput_correctOutput() {
        List<Position> listPositions;
        listPositions = worker.getPossibleBuilds();

        List<Position> listPositionResult = new ArrayList<>();
        listPositionResult.add((new Position(2, 3)));
        listPositionResult.add((new Position(2, 4)));
        listPositionResult.add((new Position(4, 4)));
        listPositionResult.add((new Position(4, 3)));
        listPositionResult.add((new Position(3, 3)));

        assertTrue(listPositionResult.equals(listPositions));
    }
}