package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.board.Space;
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
    }

    @Test
    public void getFreePositions() {

    }

    @Test
    public void arePositionsFree() {

    }

    @Test
    public void getNextSpaceInLine() {

    }

    @Test
    public void getNeighbours() {

    }

    @Test
    public void getNeighbourWorkers() {
    }

    @Test
    public void getMoveTypeByLevel() {
    }


}