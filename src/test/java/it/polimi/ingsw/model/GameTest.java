package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.parser.GodParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    private Game instance;

    @Before
    public void setUp() throws Exception {
        this.instance = Game.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        Game.resetInstance();
    }

    @Test
    public void getInstance() {
        assertEquals(instance, Game.getInstance());
    }

    @Test
    public void getPlayerByNickname_NicknameFound() {
        Player player = new Player("anna");
        instance.addPlayer(player);
        assertEquals(player, instance.getPlayerByNickname("anna"));
    }

    @Test
    public void getPlayerByNickname_NicknameNotFound() {
        assertNull(instance.getPlayerByNickname("mario"));
    }

    @Test
    public void addPlayer() {
        instance.addPlayer(new Player("sam"));
        instance.addPlayer(new Player("andre"));

        assertEquals(2, instance.getNumCurrentPlayers());
    }

    @Test
    public void setChosenMaxPlayers_inRange() {
        assertTrue(instance.setChosenMaxPlayers(3));
        assertEquals(3, instance.getChosenPlayersNumber());
    }

    @Test
    public void setChosenMaxPlayers_outOfBound() {
        assertFalse(instance.setChosenMaxPlayers(5));
    }

    @Test
    public void isNicknameTaken_True() {
        instance.addPlayer(new Player("sam"));
        assertTrue(instance.isNicknameTaken("sam"));
    }

    @Test
    public void isNicknameTaken_False() {
        assertFalse(instance.isNicknameTaken("anto"));
    }

    @Test
    public void testGameState() {
        GameState gameState = GameState.LOGIN;
        assertEquals(gameState, GameState.LOGIN);
    }

    @Test
    public void getReduceGodList() {
        List<God> godList = GodParser.parseGods();
        List<ReducedGod> reducedGodList = new ArrayList<>();
        for (God god : godList) {
            reducedGodList.add(new ReducedGod(god));
        }

        assertEquals(reducedGodList, instance.getReduceGodList());
    }

    @Test
    public void getGodByName_Found() {
        List<God> godList = GodParser.parseGods();
        String name = godList.get(0).getName();
        assertEquals(godList.get(0), instance.getGodByName(name));
    }

    @Test
    public void getGodByName_NotFound() {
        String name = "";
        assertNull(instance.getGodByName(name));
    }

    @Test
    public void getPlayersNicknames() {
        Player player1 = new Player("anna");
        Player player2 = new Player("luigi");
        instance.addPlayer(player1);
        instance.addPlayer(player2);

        assertEquals(List.of("anna", "luigi"), instance.getPlayersNicknames());
    }

    @Test
    public void getBoard() {
        assertNotNull(instance.getBoard());
    }

    @Test
    public void initWorkersOnBoard() {
        Position p1 = new Position(0, 3);
        Position p2 = new Position(1, 3);
        Worker w1 = new Worker(p1);
        Worker w2 = new Worker(p2);
        List<Worker> workerList = List.of(w1, w2);

        instance.initWorkersOnBoard(workerList);
        assertEquals(w1, instance.getWorkerByPosition(p1));
        assertEquals(w2, instance.getWorkerByPosition(p2));
    }

    @Test
    public void moveWorker() {
        Position p1 = new Position(0, 3);
        Position p2 = new Position(1, 3);
        Worker w1 = new Worker(p1);

        instance.moveWorker(w1, p2);
        assertEquals(w1, instance.getBoard().getWorkerByPosition(p2));
        assertNull(instance.getBoard().getWorkerByPosition(p1));
    }

    @Test
    public void buildBlock() {
        Position p1 = new Position(0, 3);
        Position p2 = new Position(1, 3);
        Worker w1 = new Worker(p1);

        instance.buildBlock(w1, p2);
        assertEquals(1, instance.getBoard().getSpace(p2).getLevel());
    }

    @Test
    public void getReducedSpaceBoard() {
        ReducedSpace[][] s1 = instance.getReducedSpaceBoard();

        for (int i = 0; i < Board.MAX_ROWS; i++) {
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                assertNotNull(s1[i][j]);
            }
        }
    }

    @Test
    public void getFreePositions() {
        List<Position> expected = new ArrayList<>();
        for (int i = 0; i < Board.MAX_ROWS; i++) {
            for (int j = 0; j < Board.MAX_COLUMNS; j++) {
                expected.add(new Position(i, j));
            }
        }
        assertEquals(expected, instance.getFreePositions());
    }

    @Test
    public void arePositionsFree() {
        List<Position> positionList = List.of(new Position(0, 0), new Position(0, 1));
        assertTrue(instance.arePositionsFree(positionList));
    }

    @Test
    public void getNextSpaceInLine() {
        Position orig = new Position(2, 2);
        Position dest = new Position(1, 1);
        Space target = instance.getNextSpaceInLine(orig, dest);

        assertEquals(instance.getBoard().getSpace(0, 0), target);
    }

    @Test
    public void getNeighbours() {
        List<Position> expectedPositions = new ArrayList<>();
        expectedPositions.add(new Position(3, 3));
        expectedPositions.add(new Position(3, 4));
        expectedPositions.add(new Position(4, 3));

        assertEquals(expectedPositions, instance.getNeighbours(new Position(4, 4)));
    }

    @Test
    public void getNeighbourWorkers() {
        Worker w1 = new Worker(new Position(0, 0));
        Worker w2 = new Worker(new Position(3, 4));
        w1.setColor(Color.BLUE);
        w2.setColor(Color.RED);

        instance.getBoard().getSpace(0, 0).setWorker(w1);
        instance.getBoard().getSpace(3, 4).setWorker(w2);

        assertEquals(List.of(), instance.getNeighbourWorkers(w1.getPosition(), false));

    }

    @Test
    public void getMoveTypeByLevel_LevelUp() {
        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);
        instance.getBoard().getSpace(p2).increaseLevel(1);

        assertEquals(MoveType.UP, instance.getMoveTypeByLevel(p1, p2));
    }

    @Test
    public void getEnemyWorkers() {
        Player p1 = new Player("mario");
        Player p2 = new Player("luigi");

        Worker w1 = new Worker(Color.RED);
        Worker w2 = new Worker(Color.RED);
        p1.addWorker(w1);
        p1.addWorker(w2);
        p1.initWorkers(List.of(new Position(3, 3), new Position(3, 1)));

        Worker w3 = new Worker(Color.BLUE);
        Worker w4 = new Worker(Color.BLUE);
        p2.addWorker(w3);
        p2.addWorker(w4);
        p2.initWorkers(List.of(new Position(0, 0), new Position(1, 1)));

        instance.getBoard().initWorkers(List.of(w1, w2, w3, w4));

        instance.addPlayer(p1);
        instance.addPlayer(p2);
        assertEquals(List.of(w3, w4), instance.getEnemyWorkers(w1));
    }
}