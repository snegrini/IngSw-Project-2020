package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.MoveType;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests the {@link GameController} behaviour by simulating more matches with different status.
 * Methods are also tested.
 */
public class GameControllerTest {

    private Game game;
    private GameController gameController;
    private ClientHandler clientHandler;
    private StorageData storageData;

    String p1 = "AndreaLanzi";
    String p2 = "SamueleNegrini";
    String p3 = "SamuelKala";

    @Before
    public void setUp() {
        gameController = new GameController();
        game = Game.getInstance();

        storageData = new StorageData();

        clientHandler = new ClientHandler() {
            @Override
            public boolean isConnected() {
                return true;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void sendMessage(Message message) {

            }
        };

        connectAndSetup(p1, p2, p3);
    }

    @After
    public void tearDown() {
        // clear saved file.
        storageData.delete();

        Game.resetInstance();

        gameController = null;
        clientHandler = null;
        storageData = null;
    }

    private void connectAndSetup(String p1, String p2, String p3) {
        LoginRequest loginRequest = new LoginRequest(p1);
        gameController.onMessageReceived(loginRequest);
        PlayerNumberReply playerNumberReply = new PlayerNumberReply(p1, 3);
        gameController.onMessageReceived(playerNumberReply);
        LoginRequest loginRequestSamuele = new LoginRequest(p2);
        gameController.onMessageReceived(loginRequestSamuele);
        LoginRequest loginRequestSamuel = new LoginRequest(p3);
        gameController.onMessageReceived(loginRequestSamuel);

        Server server = new Server(gameController);
        server.addClient(p1, clientHandler);
        server.addClient(p2, clientHandler);
        server.addClient(p3, clientHandler);


    }

    @Test
    public void onMessageReceived_FirstMatch() {


        // Challenger pick up 3 gods.
        ReducedGod rg1 = new ReducedGod(new God.Builder("Apollo").build());
        ReducedGod rg2 = new ReducedGod(new God.Builder("Athena").build());
        ReducedGod rg3 = new ReducedGod(new God.Builder("Minotaur").build());
        List<ReducedGod> godList = List.of(rg1, rg2, rg3);
        GodListMessage firstGodListMessage = new GodListMessage(p1, godList, 0);
        gameController.onMessageReceived(firstGodListMessage);
        assertEquals(godList, gameController.getAvailableGods());

        // Users pick up their own gods. (p2-> minotaur, p3-> athena, p1-> apollo)
        GodListMessage one_godListMessage = new GodListMessage(p2, List.of(godList.get(2)), 0);
        gameController.onMessageReceived(one_godListMessage);
        GodListMessage two_godListMessage = new GodListMessage(p3, List.of(godList.get(1)), 0);
        gameController.onMessageReceived(two_godListMessage);
        GodListMessage three_godListMessage = new GodListMessage(p1, List.of(godList.get(0)), 0);
        gameController.onMessageReceived(three_godListMessage);

        // Challenger pick which is the first player.
        MatchInfoMessage firstPlayer_MatchInfoMessage = new MatchInfoMessage(p1, MessageType.PICK_FIRST_PLAYER, null, null, null,"AndreaLanzi");
        gameController.onMessageReceived(firstPlayer_MatchInfoMessage);

        // Users pick up their own colors and set their initial workers positions.
        // Init first player.
        ColorsMessage first_colorsMessage = new ColorsMessage(p1, List.of(Color.BLUE));
        gameController.onMessageReceived(first_colorsMessage);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 1));
        PositionMessage p1_workersPositionMessage = new PositionMessage(p1, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p1_workersPositionMessage);
        positions.clear();

        // Init second player.
        ColorsMessage second_colorsMessage = new ColorsMessage(p2, List.of(Color.GREEN));
        gameController.onMessageReceived(second_colorsMessage);
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));
        PositionMessage p2_workersPositionMessage = new PositionMessage(p2, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p2_workersPositionMessage);
        positions.clear();

        // Init third player.
        ColorsMessage third_colorsMessage = new ColorsMessage(p3, List.of(Color.RED));
        gameController.onMessageReceived(third_colorsMessage);
        positions.add(new Position(1, 3));
        positions.add(new Position(1, 4));
        PositionMessage p3_workersPositionMessage = new PositionMessage(p3, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p3_workersPositionMessage);
        positions.clear();

        // Asserts of INIT phase.
        assertTrue(gameController.getAvailableGods().isEmpty());
        List<Position> checkWorkersPositions = new ArrayList<>();
        checkWorkersPositions.add(new Position(1, 3));
        checkWorkersPositions.add(new Position(1, 4));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname("SamuelKala").getWorkersPositions());
        checkWorkersPositions.clear();

        // First player's turn.
        // pick worker.
        PositionMessage p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 1)));
        gameController.onMessageReceived(p1_movingWorker);
        // move.
        PositionMessage p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(2, 2)));
        gameController.onMessageReceived(p1_apply);
        checkWorkersPositions.add(new Position(0, 0));
        checkWorkersPositions.add(new Position(2, 2));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname(p1).getWorkersPositions());
        checkWorkersPositions.clear();
        // build.
        PositionMessage p1_build = new PositionMessage(p1, MessageType.BUILD, List.of(new Position(2, 3)));
        gameController.onMessageReceived(p1_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(2, 3).getLevel());


        // Second player's turn.
        // pick worker.
        PositionMessage p2_movingWorker = new PositionMessage(p2, MessageType.PICK_MOVING_WORKER, List.of(new Position(3, 3)));
        gameController.onMessageReceived(p2_movingWorker);
        // move.
        PositionMessage p2_apply = new PositionMessage(p2, MessageType.APPLY_EFFECT, List.of(new Position(2, 4)));
        gameController.onMessageReceived(p2_apply);
        checkWorkersPositions.add(new Position(1, 1));
        checkWorkersPositions.add(new Position(2, 4));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname(p2).getWorkersPositions());
        checkWorkersPositions.clear();
        // build.
        PositionMessage p2_build = new PositionMessage(p2, MessageType.BUILD, List.of(new Position(3, 3)));
        gameController.onMessageReceived(p2_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(3, 3).getLevel());

        // Third player's turn.
        // pick worker.
        PositionMessage p3_movingWorker = new PositionMessage(p3, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p3_movingWorker);
        // move.
        PositionMessage p3_move = new PositionMessage(p3, MessageType.MOVE, List.of(new Position(2, 3)));
        gameController.onMessageReceived(p3_move);
        // move-up -> Athena FX applied to opponents
        assertTrue(game.getPlayerByNickname(p1).getWorkerByPosition(new Position(0, 0)).checkLockedMovement(MoveType.UP));
        assertTrue(game.getPlayerByNickname(p2).getWorkerByPosition(new Position(2, 4)).checkLockedMovement(MoveType.UP));
        // build.
        PositionMessage p3_build = new PositionMessage(p3, MessageType.BUILD, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p3_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(1, 3).getLevel());


        // First player's turn.
        // pick worker.
        p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(2, 2)));
        gameController.onMessageReceived(p1_movingWorker);
        // move.
        p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(1, 1)));
        gameController.onMessageReceived(p1_apply);
        checkWorkersPositions.add(new Position(0, 0));
        checkWorkersPositions.add(new Position(1, 1));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname(p1).getWorkersPositions());
        checkWorkersPositions.clear();
        // build.
        p1_build = new PositionMessage(p1, MessageType.BUILD, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p1_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(1, 2).getLevel());


        // Second player's turn.
        // pick worker.
        p2_movingWorker = new PositionMessage(p2, MessageType.PICK_MOVING_WORKER, List.of(new Position(2, 4)));
        gameController.onMessageReceived(p2_movingWorker);
        // move
        p2_apply = new PositionMessage(p2, MessageType.APPLY_EFFECT, List.of(new Position(1, 4)));
        gameController.onMessageReceived(p2_apply);
        // build
        p2_build = new PositionMessage(p2, MessageType.BUILD, List.of(new Position(2, 4)));
        gameController.onMessageReceived(p2_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(2, 4).getLevel());


        // Third player's turn.
        // pick worker.
        p3_movingWorker = new PositionMessage(p3, MessageType.PICK_MOVING_WORKER, List.of(new Position(2, 3)));
        gameController.onMessageReceived(p3_movingWorker);
        // move.
        p3_move = new PositionMessage(p3, MessageType.MOVE, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p3_move);
        // move-flat -> Athena FX not applied to opponents
        assertFalse(game.getPlayerByNickname(p1).getWorkerByPosition(new Position(0, 0)).checkLockedMovement(MoveType.UP));
        assertFalse(game.getPlayerByNickname(p2).getWorkerByPosition(new Position(1, 4)).checkLockedMovement(MoveType.UP));
        // build
        p3_build = new PositionMessage(p3, MessageType.BUILD, List.of(new Position(2, 3)));
        gameController.onMessageReceived(p3_build);
        assertEquals(2, Game.getInstance().getBoard().getSpace(2, 3).getLevel());


        // First player's turn.
        // pick worker.
        p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 1)));
        gameController.onMessageReceived(p1_movingWorker);
        // move.
        p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p1_apply);
        checkWorkersPositions.add(new Position(0, 0));
        checkWorkersPositions.add(new Position(1, 2));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname(p1).getWorkersPositions());
        checkWorkersPositions.clear();
        // build.
        p1_build = new PositionMessage(p1, MessageType.BUILD, List.of(new Position(2, 3)));
        gameController.onMessageReceived(p1_build);
        assertEquals(3, Game.getInstance().getBoard().getSpace(2, 3).getLevel());

        // Second player's turn.
        // pick worker.
        p2_movingWorker = new PositionMessage(p2, MessageType.PICK_MOVING_WORKER, List.of(new Position(2, 2)));
        gameController.onMessageReceived(p2_movingWorker);
        // move
        p2_apply = new PositionMessage(p2, MessageType.APPLY_EFFECT, List.of(new Position(3, 2)));
        gameController.onMessageReceived(p2_apply);
        // build
        p2_build = new PositionMessage(p2, MessageType.BUILD, List.of(new Position(3, 3)));
        gameController.onMessageReceived(p2_build);
        assertEquals(2, Game.getInstance().getBoard().getSpace(3, 3).getLevel());

        // Third player's turn.
        // pick worker.
        p3_movingWorker = new PositionMessage(p3, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p3_movingWorker);
        // move.
        p3_move = new PositionMessage(p3, MessageType.MOVE, List.of(new Position(0, 3)));
        gameController.onMessageReceived(p3_move);
        // move-flat -> Athena FX not applied to opponents
        assertFalse(game.getPlayerByNickname(p1).getWorkerByPosition(new Position(0, 0)).checkLockedMovement(MoveType.UP));
        assertFalse(game.getPlayerByNickname(p2).getWorkerByPosition(new Position(1, 4)).checkLockedMovement(MoveType.UP));
        // build
        p3_build = new PositionMessage(p3, MessageType.BUILD, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p3_build);
        assertEquals(2, Game.getInstance().getBoard().getSpace(1, 3).getLevel());


        // First player's turn.
        // pick worker.
        p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p1_movingWorker);
        // move.
        p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p1_apply);
        checkWorkersPositions.add(new Position(0, 0));
        checkWorkersPositions.add(new Position(1, 3));
        assertEquals(checkWorkersPositions, Game.getInstance().getPlayerByNickname(p1).getWorkersPositions());
        checkWorkersPositions.clear();
        // build.
        p1_build = new PositionMessage(p1, MessageType.BUILD, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p1_build);
        assertEquals(2, Game.getInstance().getBoard().getSpace(1, 2).getLevel());

        // Second player's turn.
        // pick worker.
        p2_movingWorker = new PositionMessage(p2, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 4)));
        gameController.onMessageReceived(p2_movingWorker);
        // move
        p2_apply = new PositionMessage(p2, MessageType.APPLY_EFFECT, List.of(new Position(2, 4)));
        gameController.onMessageReceived(p2_apply);
        // build
        p2_build = new PositionMessage(p2, MessageType.BUILD, List.of(new Position(3, 4)));
        gameController.onMessageReceived(p2_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(3, 4).getLevel());


        // Third player's turn.
        // pick worker.
        p3_movingWorker = new PositionMessage(p3, MessageType.PICK_MOVING_WORKER, List.of(new Position(0, 3)));
        gameController.onMessageReceived(p3_movingWorker);
        // move.
        p3_move = new PositionMessage(p3, MessageType.MOVE, List.of(new Position(0, 2)));
        gameController.onMessageReceived(p3_move);
        // move-flat -> Athena FX not applied to opponents
        assertFalse(game.getPlayerByNickname(p1).getWorkerByPosition(new Position(0, 0)).checkLockedMovement(MoveType.UP));
        assertFalse(game.getPlayerByNickname(p2).getWorkerByPosition(new Position(2, 4)).checkLockedMovement(MoveType.UP));
        // build
        p3_build = new PositionMessage(p3, MessageType.BUILD, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p3_build);
        assertEquals(3, Game.getInstance().getBoard().getSpace(1, 2).getLevel());


        // First player's turn.
        // pick worker.
        p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(1, 3)));
        gameController.onMessageReceived(p1_movingWorker);
        // move.
        p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(1, 2)));
        gameController.onMessageReceived(p1_apply);

        assertEquals(true, gameController.isGameFinished());

    }

    @Test
    public void onMessageReceived_SecondMatch() {

        //Only for test prepare effect with Prometheus.

        LoginRequest loginRequest = new LoginRequest(p1);
        gameController.onMessageReceived(loginRequest);
        PlayerNumberReply playerNumberReply = new PlayerNumberReply(p1, 3);
        gameController.onMessageReceived(playerNumberReply);
        LoginRequest loginRequestSamuele = new LoginRequest(p2);
        gameController.onMessageReceived(loginRequestSamuele);
        LoginRequest loginRequestSamuel = new LoginRequest(p3);
        gameController.onMessageReceived(loginRequestSamuel);

        Server server = new Server(gameController);
        server.addClient(p1, clientHandler);
        server.addClient(p2, clientHandler);
        server.addClient(p3, clientHandler);


        // Challenger pick up 3 gods.
        ReducedGod rg1 = new ReducedGod(new God.Builder("Prometheus").build());
        ReducedGod rg2 = new ReducedGod(new God.Builder("Athena").build());
        ReducedGod rg3 = new ReducedGod(new God.Builder("Minotaur").build());
        List<ReducedGod> godList = List.of(rg1, rg2, rg3);
        GodListMessage firstGodListMessage = new GodListMessage(p1, godList, 0);
        gameController.onMessageReceived(firstGodListMessage);
        assertEquals(godList, gameController.getAvailableGods());

        // Users pick up their own gods. (p2-> minotaur, p3-> athena, p1-> prometheus)
        GodListMessage one_godListMessage = new GodListMessage(p2, List.of(godList.get(2)), 0);
        gameController.onMessageReceived(one_godListMessage);
        GodListMessage two_godListMessage = new GodListMessage(p3, List.of(godList.get(1)), 0);
        gameController.onMessageReceived(two_godListMessage);
        GodListMessage three_godListMessage = new GodListMessage(p1, List.of(godList.get(0)), 0);
        gameController.onMessageReceived(three_godListMessage);

        // Challenger pick which is the first player.
        MatchInfoMessage firstPlayer_MatchInfoMessage = new MatchInfoMessage(p1, MessageType.PICK_FIRST_PLAYER, null, null, null,"AndreaLanzi");
        gameController.onMessageReceived(firstPlayer_MatchInfoMessage);

        // Users pick up their own colors and set their initial workers positions.
        // Init first player.
        ColorsMessage first_colorsMessage = new ColorsMessage(p1, List.of(Color.BLUE));
        gameController.onMessageReceived(first_colorsMessage);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 1));
        PositionMessage p1_workersPositionMessage = new PositionMessage(p1, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p1_workersPositionMessage);
        positions.clear();

        // Init second player.
        ColorsMessage second_colorsMessage = new ColorsMessage(p2, List.of(Color.GREEN));
        gameController.onMessageReceived(second_colorsMessage);
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 3));
        PositionMessage p2_workersPositionMessage = new PositionMessage(p2, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p2_workersPositionMessage);
        positions.clear();

        // Init third player.
        ColorsMessage third_colorsMessage = new ColorsMessage(p3, List.of(Color.RED));
        gameController.onMessageReceived(third_colorsMessage);
        positions.add(new Position(1, 3));
        positions.add(new Position(1, 4));
        PositionMessage p3_workersPositionMessage = new PositionMessage(p3, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p3_workersPositionMessage);
        positions.clear();

        // First player's turn.
        // pick worker.
        PositionMessage p1_movingWorker = new PositionMessage(p1, MessageType.PICK_MOVING_WORKER, List.of(new Position(0, 0)));
        gameController.onMessageReceived(p1_movingWorker);
        // enable effect
        PrepareEffectMessage p1_prepareEffect = new PrepareEffectMessage(p1, true);
        gameController.onMessageReceived(p1_prepareEffect);
        // build.
        PositionMessage p1_apply = new PositionMessage(p1, MessageType.APPLY_EFFECT, List.of(new Position(0, 1)));
        gameController.onMessageReceived(p1_apply);

        // move.
        PositionMessage p1_move = new PositionMessage(p1, MessageType.MOVE, List.of(new Position(1, 0)));
        gameController.onMessageReceived(p1_move);

        // build.
        PositionMessage p1_build = new PositionMessage(p1, MessageType.BUILD, List.of(new Position(0, 0)));
        gameController.onMessageReceived(p1_build);
        assertEquals(1, Game.getInstance().getBoard().getSpace(0, 0).getLevel());
        assertEquals(1, Game.getInstance().getBoard().getSpace(0, 1).getLevel());

    }

    @Test
    public void addVirtualView() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        assertNotNull(gameController.getVirtualViewMap().get("testNickname"));
    }


    @Test
    public void removeVirtualView() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        gameController.removeVirtualView("testNickname", true);
        assertNull(gameController.getVirtualViewMap().get("testNickname"));
    }

    @Test
    public void getColorList() {
        List<Color> colors = gameController.getColorList();
        assertEquals(Color.BLUE, colors.get(0));
        assertEquals(Color.RED, colors.get(1));
        assertEquals(Color.GREEN, colors.get(2));
    }

    @Test
    public void getAvailableColors() {
        List<Color> colors = gameController.getAvailableColors();
        assertEquals(Color.BLUE, colors.get(0));
        assertEquals(Color.RED, colors.get(1));
        assertEquals(Color.GREEN, colors.get(2));
    }

    @Test
    public void getVirtualViewMap() {
        VirtualView virtualView = new VirtualView(clientHandler);
        gameController.addVirtualView("testNickname", virtualView);
        assertNotNull(gameController.getVirtualViewMap());

    }
}

