package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.EffectType;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.VirtualView;

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
            case INIT_COLORS:
                if (inputController.check(receivedMessage)) {
                    colorHandler((ColorsMessage) receivedMessage);
                }
                break;
            case INIT_WORKERSPOSITIONS:
                if (inputController.check(receivedMessage)) {
                    workerPositionsHandler((PositionMessage) receivedMessage);
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
                    pickWorkerHandler(receivedMessage, virtualView);
                }
                break;
            case MOVE: // TODO input controller
                moveHandler((PositionMessage) receivedMessage, virtualView);
                break;
            case BUILD: // TODO input controller
                buildHandler((PositionMessage) receivedMessage, virtualView);
                break;
            case ENABLE_EFFECT:
                prepareEffect((PrepareEffectMessage) receivedMessage);
                break;
            case APPLY_EFFECT: // TODO apply effect
                break;
            default:
                // TODO show exception
                break;
        }
    }


    private void pickWorkerHandler(Message receivedMessage, VirtualView virtualView) {

        turnController.setActiveWorker(game.getPlayerByNickname(receivedMessage
                .getNickname()).getWorkerByPosition((((PositionMessage) receivedMessage)
                .getPositionList().get(0))));

        movePhase();
    }

    private void movePhase() {


        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        turnController.setPhaseType(EffectType.YOUR_MOVE);

        // EFFECT REQUIRE YOUR MOVE

        if (checkEffectPhase(turnController.getPhaseType())) {
            if (requireEffect()) {
                virtualView.askEnableEffect();
            } else {
                virtualView.askMove(turnController.getActiveWorker().getPossibleMoves());
            }
        } else {
            virtualView.askMove(turnController.getActiveWorker().getPossibleMoves());
        }
    }


    private boolean requireEffect() {
        return game.getPlayerByNickname(turnController.getActivePlayer()).getGod()
                .getEffectByType(turnController.getPhaseType())
                .require(turnController.getActiveWorker());
    }

    private void prepareEffect(PrepareEffectMessage receivedMessage) {
        if (receivedMessage.isEnableEffect()) {
            Player player = game.getPlayerByNickname(turnController.getActivePlayer());
            player.getGod().getEffectByType(turnController.getPhaseType())
                    .prepare(turnController.getActiveWorker());

        } else {
            switch (turnController.getPhaseType()) {
                case YOUR_MOVE:
                    movePhase();
                    break;
                case YOUR_MOVE_AFTER:
                    buildPhase();
                    break;
                case YOUR_BUILD:
                    buildPhase();
                    break;
                case YOUR_BUILD_AFTER:
                    newTurn();
                    break;
            }
        }
    }


    private void buildHandler(PositionMessage receivedMessage, VirtualView virtualView) {
        Position buildOnPosition = receivedMessage.getPositionList().get(0);
        game.buildBlock(turnController.getActiveWorker(), buildOnPosition);

        // CHECK EFFECT YOUR_BUILD_AFTER
        turnController.setPhaseType(EffectType.YOUR_BUILD_AFTER);
        if (checkEffectPhase(turnController.getPhaseType())) {
            if (requireEffect()) {
                virtualView.askEnableEffect();
            } else {
                // prossimo turno.
                turnController.next();
                newTurn();
            }
        } else {
            // prossimo turno.
            turnController.next();
            newTurn();
        }


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

            turnController.setPhaseType(EffectType.YOUR_MOVE_AFTER);
            if (checkEffectPhase(turnController.getPhaseType())) {
                if (requireEffect()) {
                    virtualView.askEnableEffect();
                } else {
                    buildPhase();
                }
            } else {
                buildPhase();
            }

        }
    }

    private boolean checkEffectPhase(EffectType effectType) {
        return (game.getPlayerByNickname(turnController.getActivePlayer()).getGod().getEffectByType(effectType) != null);
    }

    private void buildPhase() {

        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        turnController.setPhaseType(EffectType.YOUR_BUILD);

        // CHECK EFFECT YOUR_BUILD
        if (checkEffectPhase(turnController.getPhaseType())) {
            if (requireEffect()) {
                virtualView.askEnableEffect();
            } else {
                virtualView.askNewBuildingPosition(turnController.getActiveWorker().getPossibleBuilds());
            }
        } else {
            virtualView.askNewBuildingPosition(turnController.getActiveWorker().getPossibleBuilds());
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

            if (!availableGods.isEmpty()) {
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
     */
    private void workerPositionsHandler(PositionMessage receivedMessage) {
        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        List<Position> positions = receivedMessage.getPositionList();

        player.initWorkers(positions);

        List<Worker> workers = new ArrayList<>();
        for (Position p : positions) {
            workers.add(player.getWorkerByPosition(p));
        }

        game.initWorkersOnBoard(workers);

        if (availableColors.size() != Game.MAX_PLAYERS - game.getChosenPlayersNumber()) {
            turnController.next();
            VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
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

        newTurn();
    }

    private void newTurn() {

        turnController.setPhaseType(EffectType.YOUR_MOVE);

        pickWorker();
    }

    private void pickWorker() {

        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        List<Position> positionList = new ArrayList<>(player.getWorkersPositions());
        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        virtualView.askMovingWorker(positionList);
    }


    void endGame() {
        // TODO send message to all players, close connections
    }


    /**
     * Adds a Player VirtualView to the controller if the first player max_players is not exceeded.
     * Then adds a controller observer to the view.
     * Adds the VirtualView to the game model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the virtualview to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        // This is the first player connecting
        if (virtualViews.size() == 0) {
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);
            game.getBoard().addObserver(virtualView);
        } else if (virtualViews.size() < game.getChosenPlayersNumber()) {
            virtualViews.put(nickname, virtualView);
            game.addObserver(virtualView);
            game.getBoard().addObserver(virtualView);
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
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
        VirtualView virtualView = virtualViews.get(turnController.getActivePlayer());
        switch (message.getMessageType()) {
            case MOVE_FX:
                virtualView.askMoveFx(((PositionMessage) message).getPositionList());
                break;
            case BUILD_FX:
                virtualView.askBuildFx(((PositionMessage) message).getPositionList());
                break;
            case WIN_FX:
                break;
        }

    }

    /**
     * Handles the disconnection of a client.
     *
     * @param clientHandler the client disconnecting.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        String nickname = virtualViews.entrySet()
                .stream()
                .filter(vv -> clientHandler.equals(vv.getValue().getClientHandler()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (nickname != null) {
            virtualViews.remove(nickname);
            notifyAllViews(nickname + " disconnected from the server. GAME ENDED.");
            // TODO end the game.
        }
    }
}
