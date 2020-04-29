package controller;

import model.Game;
import model.God;
import model.ReducedGod;
import model.board.Position;
import model.enumerations.Color;
import model.enumerations.GameState;
import model.player.Player;
import model.player.Worker;
import network.message.*;
import view.VirtualView;

import java.util.*;

public class GameController {

    private Game game;
    private Map<String, VirtualView> virtualViews;

    private GameState gameState;
    private List<ReducedGod> availableGods;
    private List<Color> availableColors;


    private List<ReducedGod> activeGodList; // TODO in Game create getActiveGodList (wich take only gods assigned to players)


    private TurnController turnController;
    private InputController inputController;

    public GameController() {
        this.game = Game.getInstance();
        availableColors = getColorList();

        this.virtualViews = Collections.synchronizedMap(new HashMap<>());

        inputController = new InputController(virtualViews, this);

        gameState = GameState.LOGIN; // Initialize Game State.
    }

    public TurnController getTurnController() {
        return turnController;
    }

    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViews.get(receivedMessage.getNickname());

        switch (gameState) {
            case LOGIN:
                loginState(receivedMessage, virtualView);
                break;
            case INIT:
                initState(receivedMessage, virtualView);
                break;
            case IN_GAME:
                inGameState(receivedMessage, virtualView);
                break;
            default: // Should never reach this condition
                break;
        }
    }


    // 3 MAIN STATE METHODS:
    private void loginState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case LOGIN_REQUEST:
                if (inputController.check(receivedMessage)) {
                    loginHandler((LoginRequest) receivedMessage, virtualView);
                }
                break;
            case PLAYERNUMBER_REPLY:
                if (inputController.check(receivedMessage)) {
                    game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
                }
                break;
            default:
                // TODO show exception
                break;
        }
    }

    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case GODLIST:
                if (inputController.check(receivedMessage)) {
                    godListHandler((GodListMessage) receivedMessage);
                }
                break;
            case INIT_WORKERSPOSITIONS:
                if (inputController.check(receivedMessage)) {
                    workerPositionsHandler((WorkersPositionsMessage) receivedMessage, virtualView);
                }
                break;
            case INIT_COLORS:
                if (inputController.check(receivedMessage)) {
                    colorHandler((ColorsMessage) receivedMessage);
                }
                break;

            default:
                // TODO show exception
                break;
        }
    }

    private void inGameState(Message receivedMessage, VirtualView virtualView) {
        // check if sender's nickname is in listPlayer.
        if (!game.isNicknameTaken(receivedMessage.getNickname())) {
            virtualView.showGenericErrorMessage("Player is not in game.");
        }

        // check if sender is the active player.
        if (turnController.getActivePlayer().equals(receivedMessage.getNickname())) {
            //return new GenericErrorMessage("Not your turn.");
            virtualView.sendMessage(new GenericErrorMessage("Not your turn."));
        }

        switch (receivedMessage.getMessageType()) {
            case MOVE:
                // move((Move) receivedMessage, virtualView);
                break;
            case BUILD:
                break;
            default:
                // TODO show exception
                break;
        }
    }


    // UTILITY METHODS:

    /**
     * if first client then ask number of players he wants,  add player to the game otherwise
     * eventually change game state
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void loginHandler(LoginRequest receivedMessage, VirtualView virtualView) {
        String nickname = receivedMessage.getNickname();

        if (game.getNumCurrentPlayers() == 0) { // First player logged. Ask number of players.
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true);
            virtualView.askPlayersNumber();
        } else {
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true);

            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                initGame();
            }
        }
    }

    /**
     * Change gameStete into INIT. Initialize TurnController and asks a player to pick the gods
     */
    private void initGame() {
        gameState = GameState.INIT;
        turnController = new TurnController();

        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        virtualView.askGod(game.getReduceGodList(), game.getChosenPlayersNumber());
    }

    /**
     * If receive a godList with multiple gods then create the list.
     * If receive a single god in list then assign it to the current player.
     *
     * @param receivedMessage message from client
     */
    private void godListHandler(GodListMessage receivedMessage) {

        // if received contains a list
        if (receivedMessage.getGodList().size() > 1) {
            availableGods = new ArrayList<>(receivedMessage.getGodList());
            askGodToNextPlayer();
        } else { // else receivedMessage contains only 1 god
            God god = game.getGodByName(receivedMessage.getGodList().get(0).getName());
            game.getPlayerByNickname(receivedMessage.getNickname()).setGod(god);
            availableGods.remove(receivedMessage.getGodList().get(0));

            if (!(availableGods.size() == 0)) {
                askGodToNextPlayer();
            } else {
                // the last one who pick his god is the first one player of every round.
                askWorkersPositions(turnController.getActivePlayer());
            }
        }
    }


    private void askGodToNextPlayer() {
        // ask god to the next player
        turnController.next();
        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        virtualView.askGod(availableGods, 1); // Only 1 god requested to client.
    }

    private void askWorkersPositions(String nickname) {
        VirtualView virtualView = virtualViews.get(nickname);
        virtualView.showBoard(game.getBoard().getReducedSpaceBoard());
        virtualView.askInitWorkersPositions(game.getBoard().getFreePositions());
    }

    /**
     * Assign 2 positions to 2 workers of the player
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void workerPositionsHandler(WorkersPositionsMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());

        for (Position p : receivedMessage.getPositionList()) {
            Worker worker = new Worker(p);
            player.addWorker(worker);
            game.getBoard().getSpace(p).setWorker(worker);
        }
        virtualView.askInitWorkerColor(availableColors);

    }

    /**
     * If color picked by client is available (and not picked from another client)
     * then assign color to workers of the current player.
     *
     * @param receivedMessage message from client
     */
    private void colorHandler(ColorsMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());

        for (Worker w : player.getWorkers()) {
            w.setColor(receivedMessage.getColorList().get(0));
        }

        availableColors.remove(receivedMessage.getColorList().get(0));

        if (!(availableColors.size() == 3 - game.getChosenPlayersNumber())) {
            turnController.next();
            askWorkersPositions(turnController.getActivePlayer());
        } else {
            startGame();
        }
    }

    public void startGame() {

        // TODO notify all: game started.
    }


    void endGame() {
        // TODO send message to all players, close connections
    }


    /**
     * Adds a Player VirtualView to the controller if the first player max_players is not exceeded.
     * Then adds a controller observer to the view.
     * dds the VirtualView to the game model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the virtualview to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        // This is the first player connecting
        if (virtualViews.size() == 0) {
            // FIXME virtualView.addObserver(this);
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);

        } else if (virtualViews.size() < game.getChosenPlayersNumber()) {
            // FIXME virtualView.addObserver(this);
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);

        } else {
            virtualView.showLoginResult(true, false);
        }
    }

    /**
     * Removes a VirtualView from the controller.
     *
     * @param nickname the nickname of the VirtualView associated.
     */
    public void removeVirtualView(String nickname) {
        VirtualView vv = virtualViews.remove(nickname);
        game.removeObserver(vv);
    }


    /**
     * @return a list with all possible colors
     */
    public List<Color> getColorList() {
        List<Color> colorList = new ArrayList<>();
        colorList.add(Color.BLUE);
        colorList.add(Color.RED);
        colorList.add(Color.GREEN);
        return colorList;
    }

    public List<ReducedGod> getAvailableGods() {
        return availableGods;
    }

    public List<Color> getAvailableColors() {
        return availableColors;
    }
}
