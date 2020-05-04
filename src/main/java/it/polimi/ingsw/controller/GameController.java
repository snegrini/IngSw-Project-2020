package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.network.message.*;

import java.util.*;

public class GameController implements Observer {

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
                    godListHandler((GodListMessage) receivedMessage, virtualView);
                }
                break;
            case INIT_WORKERSPOSITIONS:
                if (inputController.check(receivedMessage)) {
                    workerPositionsHandler((PositionMessage) receivedMessage, virtualView);
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
        switch (receivedMessage.getMessageType()) {
            case PICK_MOVING_WORKER:
                if (inputController.check(receivedMessage)) {
                    virtualView.askMove(game.getPlayerByNickname(receivedMessage
                            .getNickname()).getWorkerByPosition(((PositionMessage) receivedMessage)
                            .getPositionList().get(0)).getPossibleMoves());
                    turnController.setActiveWorker(game.getPlayerByNickname(receivedMessage
                            .getNickname()).getWorkerByPosition((((PositionMessage) receivedMessage)
                            .getPositionList().get(0))));
                }
                break;
            case MOVE: // TODO input controller
                moveHandler((PositionMessage) receivedMessage, virtualView);
                break;
            case BUILD: // TODO input controller
                buildHandler((PositionMessage) receivedMessage, virtualView);
                break;
            default:
                // TODO show exception
                break;
        }
    }

    private void buildHandler(PositionMessage receivedMessage, VirtualView virtualView) {
        Position buildOnPosition = receivedMessage.getPositionList().get(0);
        game.buildBlock(turnController.getActiveWorker(), buildOnPosition);

        turnController.next();
        pickMovingWorker();
    }

    private void moveHandler(PositionMessage receivedMessage, VirtualView virtualView) {
        Position destination = receivedMessage.getPositionList().get(0);

        int origLevel = game.getSpaceLevel(turnController.getActiveWorker().getPosition());
        int destLevel = game.getSpaceLevel(destination);

        game.moveWorker(turnController.getActiveWorker(), destination);


        // Win condition:
        if (origLevel == 2 && destLevel == 3) {
            win();
        } else {
            Player p = game.getPlayerByNickname(receivedMessage.getNickname());
            virtualView.askNewBuildingPosition(p.getWorkerByPosition(destination).getPossibleBuilds());
        }
    }

    private void win() {
        notifyAllViews(turnController.getActivePlayer() + "Wins! Game Finished!");
        // TODO end game, prepare server for a new game. Set server on listen for the first client.
    }


    // UTILITY METHODS:

    /**
     * If It's the first Client then ask number of Players he wants, add Player to the Game otherwise
     * eventually change Game State
     *
     * @param receivedMessage Message from Client
     * @param virtualView     Virtual View
     */
    private void loginHandler(LoginRequest receivedMessage, VirtualView virtualView) {
        String nickname = receivedMessage.getNickname();

        if (game.getNumCurrentPlayers() == 0) { // First player logged. Ask number of players.
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, null);
            virtualView.askPlayersNumber();
        } else {
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, null);

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
    private void godListHandler(GodListMessage receivedMessage, VirtualView virtualView) {

        // if received contains a list
        if (receivedMessage.getGodList().size() > 1) {
            availableGods = new ArrayList<>(receivedMessage.getGodList());
            askGodToNextPlayer();
        } else { // else receivedMessage contains only 1 god
            God god = game.getGodByName(receivedMessage.getGodList().get(0).getName());
            god.addObserverToAllEffects(this);
            game.getPlayerByNickname(receivedMessage.getNickname()).setGod(god);
            availableGods.remove(receivedMessage.getGodList().get(0));

            if (!(availableGods.size() == 0)) {
                askGodToNextPlayer();
            } else {
                // the last one who pick his god is the first one player of every round.
                virtualView.askInitWorkerColor(availableColors);
                //askWorkersPositions(turnController.getActivePlayer());
            }
        }
    }


    /**
     * Ask to pick a God to the next Player.
     */
    private void askGodToNextPlayer() {
        // ask god to the next player
        turnController.next();
        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        virtualView.askGod(availableGods, 1); // Only 1 god requested to client.
    }

    /**
     * Ask to pick the 2 Workers' Positions to a Player.
     *
     * @param nickname nickname of the current Player.
     */
    private void askWorkersPositions(String nickname) {
        VirtualView virtualView = virtualViews.get(nickname);
        virtualView.showBoard(game.getReducedSpaceBoard());
        virtualView.askInitWorkersPositions(game.getFreePositions());
    }

    /**
     * Assign 2 positions to 2 workers of the player
     *
     * @param receivedMessage message from client
     * @param virtualView     virtual view
     */
    private void workerPositionsHandler(PositionMessage receivedMessage, VirtualView virtualView) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        List<Position> positions = receivedMessage.getPositionList();

        player.initWorkers(positions);

        List<Worker> workers = new ArrayList<>();
        for (Position p : positions) {
            workers.add(player.getWorkerByPosition(p));
        }

        game.initWorkersOnBoard(workers);

        if (!(availableColors.size() == 3 - game.getChosenPlayersNumber())) {
            turnController.next();
            virtualView = virtualViews.get(turnController.getActivePlayer());
            virtualView.askInitWorkerColor(availableColors);
        } else {
            turnController.next();
            startGame();
        }
    }

    /**
     * If color picked by client is available (and not picked from another client)
     * then assign color to workers of the current player.
     *
     * @param receivedMessage message from client
     */
    private void colorHandler(ColorsMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());

        player.addWorker(new Worker(receivedMessage.getColorList().get(0)));
        player.addWorker(new Worker(receivedMessage.getColorList().get(0)));

        availableColors.remove(receivedMessage.getColorList().get(0));
        askWorkersPositions(receivedMessage.getNickname());

    }

    private void startGame() {
        gameState = GameState.IN_GAME;
        notifyAllViews("Game Started!");

        pickMovingWorker();
    }

    private void pickMovingWorker() {
        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        List<Position> positionList = new ArrayList<>(player.getWorkersPositions());
        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        virtualView.askMovingWorker(positionList);
    }

    // TODO delete me
    /*private void refreshClientsBoards() {
        List<String> nicknames = new ArrayList<>(game.getPlayersNicknames());
        for (String s : nicknames) {
            VirtualView virtualView = virtualViews.get(s);
            virtualView.showBoard(game.getBoard().getReducedSpaceBoard());
        }
    }*/


    void endGame() {
        // TODO send message to all players, close connections
    }


    /**
     * Adds a Player VirtualView to the it.polimi.ingsw.controller if the first player max_players is not exceeded.
     * Then adds a it.polimi.ingsw.controller it.polimi.ingsw.observer to the it.polimi.ingsw.view.
     * dds the VirtualView to the game it.polimi.ingsw.model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the virtualview to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        // This is the first player connecting
        if (virtualViews.size() == 0) {
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);

        } else if (virtualViews.size() < game.getChosenPlayersNumber()) {
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);
        } else {
            virtualView.showLoginResult(true, false, null);
        }
    }

    /**
     * Removes a VirtualView from the it.polimi.ingsw.controller.
     *
     * @param nickname the nickname of the VirtualView associated.
     */
    public void removeVirtualView(String nickname) {
        VirtualView vv = virtualViews.remove(nickname);
        game.removeObserver(vv);
    }


    private void notifyAllViews(String messageToNotify) {
        for (VirtualView vv : virtualViews.values()) {
            vv.showGenericMessage(messageToNotify);
        }
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

    /**
     * Receives an update message from the effect model.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {

    }
}
