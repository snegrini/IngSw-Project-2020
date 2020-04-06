package model.player;

import groovy.lang.DelegatesTo;
import model.Game;
import model.board.Board;
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
    public void move() {
        Position position = new Position(3,4);
        worker.move(position);
        assertEquals(position, worker.getPosition());
    }

    @Test
    public void checkLevel_incompatibleLevels_false() {
        Board board = Game.getInstance().getBoard();
        Position position1 = new Position(3,4);
        Position position2 = new Position(3,3);
        board.getSpace(position2).increaseLevel(2);
        boolean result = worker.checkLevel(position1,position2);

        assertFalse(result);
    }

    @Test
    public void checkLevel_compatibleLevels_true() {
        Board board = Game.getInstance().getBoard();
        Position position1 = new Position(3,4);
        Position position2 = new Position(3,3);
        board.getSpace(position1).increaseLevel(2);
        boolean result = worker.checkLevel(position1,position2);

        assertTrue(result);
    }

    @Test
    public void getPossibleBuilds() {
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

    @Test
    public void getPossibleMoves_correctInput_correctOutput() {


        Board board = Game.getInstance().getBoard();
        board.getSpace(new Position(3,3)).increaseLevel(2);

        List<Position> listPositions;
        listPositions = worker.getPossibleMoves();

        List<Position> listPositionResult = new ArrayList<>();
        listPositionResult.add((new Position(2, 3)));
        listPositionResult.add((new Position(2, 4)));
        listPositionResult.add((new Position(4, 4)));
        listPositionResult.add((new Position(4, 3)));
        //(3,3) is not in the list of possible moves because of incompatible levels.
        //listPositionResult.add((new Position(3, 3)));

        assertTrue(listPositionResult.equals(listPositions));
    }
}