package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.BuildAgainDecorator;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.effects.MoveOverDecorator;
import it.polimi.ingsw.model.effects.SimpleEffect;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.PlayerState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the {@link Player} methods.
 */
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player("sam");
        player.setState(PlayerState.START);
    }

    @After
    public void tearDown() {
        player = null;
        Game.resetInstance();
    }

    @Test
    public void getNickname() {
        assertEquals("sam", player.getNickname());
    }

    @Test
    public void getWorkerByPosition() {
        Worker worker = new Worker(new Position(0, 0));
        player.addWorker(worker);

        assertEquals(worker, player.getWorkerByPosition(new Position(0, 0)));
    }

    @Test
    public void godGetterAndSetter() {
        God god = new God.Builder("Name Test")
                .withCaption("Caption Test")
                .withDescription("Description Test")
                .withEffects(List.of(new SimpleEffect(PhaseType.YOUR_BUILD)))
                .build();

        player.setGod(god);
        assertEquals(god, player.getGod());
    }

    @Test
    public void getValidWorkersPositions_OneInvalid_CheckEffect_Requireable() {
        Board board = Game.getInstance().getBoard();
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.BLUE);
        player.addWorker(w1);
        player.addWorker(w2);

        Worker enemyWorker = new Worker(new Position(2, 3));
        enemyWorker.setColor(Color.RED);

        // Setup a god for the player
        Effect effect = new MoveOverDecorator(new SimpleEffect(PhaseType.YOUR_MOVE), null, false, true);
        God god = new God.Builder("Apollo")
                .withEffects(List.of(effect))
                .build();
        player.setGod(god);

        // Setup worker on board
        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(3, 4).setWorker(w2);

        // Fill up the board to create an extreme condition.
        board.getSpace(2, 3).setWorker(enemyWorker);
        board.getSpace(2, 4).setDome(true);
        board.getSpace(3, 3).setDome(true);
        board.getSpace(4, 3).setDome(true);
        board.getSpace(4, 4).setDome(true);

        // Both workers expected
        List<Position> expectedPositionList = new ArrayList<>();
        expectedPositionList.add(w1.getPosition());
        expectedPositionList.add(w2.getPosition());

        assertEquals(expectedPositionList, player.getValidWorkersPositions());
    }

    @Test
    public void getValidWorkersPositions_OneInvalid_CheckEffect_NoEffect() {
        Board board = Game.getInstance().getBoard();
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.BLUE);
        player.addWorker(w1);
        player.addWorker(w2);

        Worker enemyWorker = new Worker(new Position(2, 3));
        enemyWorker.setColor(Color.RED);

        // Setup a god for the player
        Effect effect = new BuildAgainDecorator(new SimpleEffect(PhaseType.YOUR_BUILD), null, false, true, false);
        God god = new God.Builder("Name Test")
                .withEffects(List.of(effect))
                .build();
        player.setGod(god);

        // Setup worker on board
        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(3, 4).setWorker(w2);

        // Fill up the board to create an extreme condition.
        board.getSpace(2, 3).setWorker(enemyWorker);
        board.getSpace(2, 4).setDome(true);
        board.getSpace(3, 3).setDome(true);
        board.getSpace(4, 3).setDome(true);
        board.getSpace(4, 4).setDome(true);

        // Only one worker expected
        List<Position> expectedPositionList = new ArrayList<>();
        expectedPositionList.add(w1.getPosition());

        assertEquals(expectedPositionList, player.getValidWorkersPositions());
    }

    @Test
    public void getValidWorkersPositions_OneInvalid_CheckEffect_NotRequireable() {
        Board board = Game.getInstance().getBoard();
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        player.addWorker(w1);
        player.addWorker(w2);

        // Setup a god for the player
        Effect effect = new MoveOverDecorator(new SimpleEffect(PhaseType.YOUR_MOVE), null, false, true);
        God god = new God.Builder("Apollo")
                .withEffects(List.of(effect))
                .build();
        player.setGod(god);

        // Setup worker on board
        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(3, 4).setWorker(w2);

        // Fill up the board to create an extreme condition.
        board.getSpace(2, 3).setDome(true);
        board.getSpace(2, 4).setDome(true);
        board.getSpace(3, 3).setDome(true);
        board.getSpace(4, 3).setDome(true);
        board.getSpace(4, 4).setDome(true);

        // Only one worker expected
        List<Position> expectedPositionList = new ArrayList<>();
        expectedPositionList.add(w1.getPosition());

        assertEquals(expectedPositionList, player.getValidWorkersPositions());
    }

    @Test
    public void getValidWorkersPositions_BorderCase() {
        Board board = Game.getInstance().getBoard();

        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        player.addWorker(w1);
        player.addWorker(w2);

        // Setup worker on board
        board.getSpace(0, 0).setWorker(w1);
        board.getSpace(3, 4).setWorker(w2);

        // Fill up the board to create an extreme condition.
        board.getSpace(2, 3).setDome(true);
        board.getSpace(2, 4).setDome(true);
        board.getSpace(3, 3).setDome(true);
        board.getSpace(4, 3).setDome(true);

        // Both workers expected
        List<Position> expectedPositionList = new ArrayList<>();
        expectedPositionList.add(w1.getPosition());
        expectedPositionList.add(w2.getPosition());

        assertEquals(expectedPositionList, player.getValidWorkersPositions());
    }

    @Test
    public void getState() {
        assertEquals(PlayerState.START, player.getState());
    }

    @Test
    public void setState() {
        player.setState(PlayerState.BUILD);
        assertEquals(PlayerState.BUILD, player.getState());
    }

    @Test
    public void initWorkers_getWorkersPositions() {
        player.addWorker(new Worker(Color.BLUE));
        player.addWorker(new Worker(Color.BLUE));

        List<Position> positionList = List.of(new Position(0, 0), new Position(2, 4));
        player.initWorkers(positionList);
        assertEquals(positionList, player.getWorkersPositions());
    }

    @Test
    public void testEquals() {
        Player otherPlayer = new Player("sam");
        assertEquals(player, otherPlayer);
    }

    @Test
    public void testHashCode() {
        int hash = Objects.hash("sam");
        assertEquals(hash, player.hashCode());
    }
}