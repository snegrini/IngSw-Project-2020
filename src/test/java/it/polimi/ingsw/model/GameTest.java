package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Space;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.model.enumerations.TargetType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.utils.GodParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests the {@link Game} methods.
 */
public class GameTest {

    private Game instance;

    @Before
    public void setUp() {
        this.instance = Game.getInstance();

        Player p1 = new Player("mario");
        Player p2 = new Player("anna");

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

        this.instance.initWorkersOnBoard(List.of(w1, w2, w3, w4));
        this.instance.addPlayer(p1);
        this.instance.addPlayer(p2);
    }

    @After
    public void tearDown() {
        Game.resetInstance();
    }

    @Test
    public void getInstance() {
        assertEquals(instance, Game.getInstance());
    }

    @Test
    public void getPlayerByNickname_NicknameFound() {
        assertNotNull(instance.getPlayerByNickname("anna"));
    }

    @Test
    public void getPlayerByNickname_NicknameNotFound() {
        assertNull(instance.getPlayerByNickname("antonio"));
    }

    @Test
    public void addPlayer() {
        instance.addPlayer(new Player("samuele"));

        assertEquals(3, instance.getNumCurrentPlayers());
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
        assertTrue(instance.isNicknameTaken("mario"));
    }

    @Test
    public void isNicknameTaken_False() {
        assertFalse(instance.isNicknameTaken("antonio"));
    }

    @Test
    public void testGameState() {
        GameState gameState = GameState.LOGIN;
        assertEquals(GameState.LOGIN, gameState);
    }

    @Test
    public void getReduceGodList() {
        List<God> godList = GodParser.parseGods("gods.xml");
        List<ReducedGod> reducedGodList = new ArrayList<>();
        for (God god : godList) {
            reducedGodList.add(new ReducedGod(god));
        }

        assertEquals(reducedGodList, instance.getReduceGodList());
    }

    @Test
    public void getGodByName_Found() {
        List<God> godList = GodParser.parseGods("gods.xml");
        String name = godList.get(0).getName();
        assertEquals(godList.get(0), instance.getGodByName(name));
    }

    @Test
    public void getGodByName_NotFound() {
        String name = "";
        assertNull(instance.getGodByName(name));
    }

    @Test
    public void getGods() {
        List<God> parserGodList = GodParser.parseGods("gods.xml");
        List<God> gameGodList = instance.getGods();

        assertEquals(parserGodList, gameGodList);
    }

    @Test
    public void getPlayersNicknames() {
        assertEquals(List.of("mario", "anna"), instance.getPlayersNicknames());
    }

    @Test
    public void getBoard() {
        assertNotNull(instance.getBoard());
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
    public void removeWorkers() {
        Player player = instance.getPlayers().get(0);
        List<Position> workerPositionList = player.getWorkersPositions();
        assertNotNull(workerPositionList);
        assertNotEquals(List.of(), workerPositionList);

        instance.removeWorkers(player.getNickname());
        for (Position pos : workerPositionList) {
            assertNull(instance.getWorkerByPosition(pos));
        }
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
        List<Position> unexpected = new ArrayList<>();
        unexpected.add(new Position(3, 3));
        unexpected.add(new Position(3, 1));
        unexpected.add(new Position(0, 0));
        unexpected.add(new Position(1, 1));

        assertNotEquals(unexpected, instance.getFreePositions());
    }

    @Test
    public void arePositionsFree() {
        List<Position> positionList = List.of(new Position(2, 0), new Position(0, 1));
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
        Worker w1 = instance.getWorkerByPosition(new Position(0, 0));
        Position p2 = new Position(1, 1);

        assertEquals(List.of(p2), instance.getNeighbourWorkers(w1.getPosition(), false));
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
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w3 = instance.getWorkerByPosition(new Position(0, 0));
        Worker w4 = instance.getWorkerByPosition(new Position(1, 1));

        assertEquals(List.of(w3, w4), instance.getEnemyWorkers(w1));
    }

    @Test
    public void getAllyWorkers() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w2 = instance.getWorkerByPosition(new Position(3, 1));

        assertEquals(List.of(w1, w2), instance.getAllyWorkers(w1));
    }

    @Test
    public void getOtherWorker_Found() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w2 = instance.getWorkerByPosition(new Position(3, 1));

        assertEquals(w2, instance.getOtherWorker(w1));
    }

    @Test
    public void getWorkersByTargetType_AllYourWorkers() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w2 = instance.getWorkerByPosition(new Position(3, 1));

        assertEquals(List.of(w1, w2), instance.getWorkersByTargetType(w1, TargetType.ALL_YOUR_WORKERS));
    }

    @Test
    public void getWorkersByTargetType_AllOppWorkers() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w3 = instance.getWorkerByPosition(new Position(0, 0));
        Worker w4 = instance.getWorkerByPosition(new Position(1, 1));

        assertEquals(List.of(w3, w4), instance.getWorkersByTargetType(w1, TargetType.ALL_OPP_WORKERS));
    }

    @Test
    public void getWorkersByTargetType_YourActiveWorker() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));

        assertEquals(List.of(w1), instance.getWorkersByTargetType(w1, TargetType.YOUR_ACTIVE_WORKER));
    }

    @Test
    public void getWorkersByTargetType_YourWorker() {
        Worker w1 = instance.getWorkerByPosition(new Position(3, 3));
        Worker w2 = instance.getWorkerByPosition(new Position(3, 1));

        assertEquals(List.of(w2), instance.getWorkersByTargetType(w1, TargetType.YOUR_WORKER));
    }
}