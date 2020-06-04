package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.VirtualView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class GameControllerTest {

    private Game game;
    private GameController gameController;
    private ClientHandler clientHandler;

    @Before
    public void setUp() {
        gameController = new GameController();

        game = Game.getInstance();
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
    }

    @After
    public void tearDown() throws Exception {
        Game.resetInstance();
    }

    @Test
    public void onMessageReceived() {
        String p1 = "AndreaLanzi";
        String p2 = "SamueleNegrini";
        String p3 = "SamuelKala";

        LoginRequest loginRequest = new LoginRequest(p1);
        gameController.onMessageReceived(loginRequest);
        PlayerNumberReply playerNumberReply = new PlayerNumberReply(p1, 3);
        gameController.onMessageReceived(playerNumberReply);
        LoginRequest loginRequestSamuele = new LoginRequest(p2);
        gameController.onMessageReceived(loginRequestSamuele);
        LoginRequest loginRequestSamuel = new LoginRequest(p3);
        gameController.onMessageReceived(loginRequestSamuel);
        // TODO gestire i loginHandler che inizializzano il tutto.
        Server server = new Server(gameController);
        server.addClient(p1, clientHandler);
        server.addClient(p2, clientHandler);
        server.addClient(p3, clientHandler);


        // Challenger pick up 3 gods.
        List<ReducedGod> godList = new ArrayList<>();
        God.Builder godBuilder1 = new God.Builder("Apollo");
        godList.add(new ReducedGod(godBuilder1.build()));
        God.Builder godBuilder2 = new God.Builder("Athena");
        godList.add(new ReducedGod(godBuilder2.build()));
        God.Builder godBuilder3 = new God.Builder("Minotaur");
        godList.add(new ReducedGod(godBuilder3.build()));
        GodListMessage firstGodListMessage = new GodListMessage(p1, godList, 0);
        gameController.onMessageReceived(firstGodListMessage);

        // Users pick up their own gods. (p2-> minotaur, p3-> athena, p1-> apollo)
        GodListMessage one_godListMessage = new GodListMessage(p2, List.of(godList.get(2)), 0);
        gameController.onMessageReceived(one_godListMessage);
        GodListMessage two_godListMessage = new GodListMessage(p3, List.of(godList.get(1)), 0);
        gameController.onMessageReceived(two_godListMessage);
        GodListMessage three_godListMessage = new GodListMessage(p1, List.of(godList.get(0)), 0);
        gameController.onMessageReceived(three_godListMessage);

        // Challenger pick which is the first player.
        UsersInfoMessage firstPlayer_UsersInfoMessage = new UsersInfoMessage(p1, MessageType.PICK_FIRST_PLAYER, null, null, "AndreaLanzi");
        gameController.onMessageReceived(firstPlayer_UsersInfoMessage);

        // Users pick up their own colors and set their initial workers positions.
        // Init first player.
        ColorsMessage first_colorsMessage = new ColorsMessage(p1, List.of(Color.BLUE));
        gameController.onMessageReceived(first_colorsMessage);
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(0, 1));
        PositionMessage p1_workersPositionMessage = new PositionMessage(p1, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p1_workersPositionMessage);
        positions.removeAll(positions);

        // Init second player.
        ColorsMessage second_colorsMessage = new ColorsMessage(p2, List.of(Color.GREEN));
        gameController.onMessageReceived(second_colorsMessage);
        positions.add(new Position(0,2));
        positions.add(new Position(1,2));
        PositionMessage p2_workersPositionMessage = new PositionMessage(p2, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p2_workersPositionMessage);
        positions.removeAll(positions);

        // Init third player.
        ColorsMessage third_colorsMessage = new ColorsMessage(p3, List.of(Color.RED));
        gameController.onMessageReceived(third_colorsMessage);
        positions.add(new Position(1,0));
        positions.add(new Position(1,1));
        PositionMessage p3_workersPositionMessage = new PositionMessage(p3, MessageType.INIT_WORKERSPOSITIONS, positions);
        gameController.onMessageReceived(p3_workersPositionMessage);
        positions.removeAll(positions);

        // Asserts of INIT phase.
       assertTrue(gameController.getAvailableGods().isEmpty());
       List<Position> checkWorkersPositions = new ArrayList<>();
       checkWorkersPositions.add(new Position(1,0));
       checkWorkersPositions.add(new Position(1,1));
       assertTrue(Game.getInstance().getPlayerByNickname("SamuelKala").getWorkersPositions().equals(checkWorkersPositions));
        checkWorkersPositions.removeAll(checkWorkersPositions);

       // First player LOSE.

        //Second player moves.
        PositionMessage p2_movingWorker = new PositionMessage(p2, MessageType.PICK_MOVING_WORKER,List.of(new Position(0,2)));
        gameController.onMessageReceived(p2_movingWorker);
        PrepareEffectMessage p2_notApply = new PrepareEffectMessage(p2, false);
        gameController.onMessageReceived(p2_notApply);
        PositionMessage p2_move = new PositionMessage(p2, MessageType.MOVE, List.of(new Position(0,1)));
        gameController.onMessageReceived(p2_move);
        checkWorkersPositions.add(new Position(0,1));
        checkWorkersPositions.add(new Position(1,2));
        assertTrue(Game.getInstance().getPlayerByNickname("SamueleNegrini").getWorkersPositions().equals(checkWorkersPositions));




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
        gameController.removeVirtualView("testNickname");
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
    public void getAvailableGods() {
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
