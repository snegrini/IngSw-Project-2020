package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.StorageData;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.network.message.MessageType.PLAYERNUMBER_REPLY;

/**
 * This class controls the evolution of the {@link Game}.
 * Messages are read and responses are elaborated.
 */
public class GameController implements Observer, Serializable {
    private static final long serialVersionUID = 4951303731052728724L;

    private Game game;
    private transient Map<String, VirtualView> virtualViewMap;

    private GameState gameState;
    private TurnController turnController;
    private InputController inputController;

    private transient List<ReducedGod> availableGods;
    private transient List<Color> availableColors;

    private static final String STR_INVALID_STATE = "Invalid game state!";


    public static final String SAVED_GAME_FILE = "match.bless";

    /**
     * Controller of the Game.
     */
    public GameController() {
        initGameController();
    }

    /**
     * Initialize Game Controller.
     */
    public void initGameController() {
        this.game = Game.getInstance();
        this.availableColors = getColorList();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        setGameState(GameState.LOGIN);

    }

    /**
     * Switch on Game State.
     *
     * @param receivedMessage Message from Active Player.
     */
    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
            switch (gameState) {
                case LOGIN:
                    loginState(receivedMessage);
                    break;
                case INIT:
                    if(inputController.checkUser(receivedMessage)) {
                        initState(receivedMessage, virtualView);
                    }
                    break;
                case IN_GAME:
                    if(inputController.checkUser(receivedMessage)) {
                    inGameState(receivedMessage);
                    }
                    break;
                default: // Should never reach this condition
                    Server.LOGGER.warning(STR_INVALID_STATE);
                    break;
            }
    }


    // 3 MAIN STATE METHODS:

    /**
     * Switch on Login Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void loginState(Message receivedMessage) {
        if (receivedMessage.getMessageType() == PLAYERNUMBER_REPLY) {
            if (inputController.verifyReceivedData(receivedMessage)) {
                game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
                broadcastGenericMessage("Waiting for other Players . . .");
            }
        } else {
            Server.LOGGER.warning("Wrong message received from client.");
        }
    }

    /**
     * Switch on initialization's Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     * @param virtualView     Virtual View the Active PLayer.
     */
    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            case GODLIST:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    godListHandler((GodListMessage) receivedMessage, virtualView);
                }
                break;
            case PICK_FIRST_PLAYER:
                if (inputController.checkFirstPlayerHandler(receivedMessage)) {
                    pickFirstPlayerHandler(((MatchInfoMessage) receivedMessage).getActivePlayerNickname());
                }
                break;
            case INIT_COLORS:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    colorHandler((ColorsMessage) receivedMessage);
                }
                break;
            case INIT_WORKERSPOSITIONS:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    workerPositionsHandler((PositionMessage) receivedMessage);
                }
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    /**
     * Switch on Game Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void inGameState(Message receivedMessage) {
        switch (receivedMessage.getMessageType()) {
            case PICK_MOVING_WORKER:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    pickWorkerHandler(receivedMessage);
                }
                break;
            case MOVE:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    moveHandler((PositionMessage) receivedMessage);
                }
                break;
            case BUILD:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    buildHandler((PositionMessage) receivedMessage);
                }
                break;
            case ENABLE_EFFECT:
                prepareEffect((PrepareEffectMessage) receivedMessage);
                break;
            case APPLY_EFFECT:
                applyEffect((PositionMessage) receivedMessage);
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }


    /**
     * Apply Player's God's Effect then clear.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void applyEffect(PositionMessage receivedMessage) {
        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        Effect effect = player.getGod().getEffectByType(turnController.getPhaseType());
        Position positionApply = receivedMessage.getPositionList().get(0);
        effect.apply(turnController.getActiveWorker(), positionApply);
        turnController.setAppliedEffect(effect);

        if (player.getGod().getName().equals("Prometheus")) {
            turnController.resumePhase();
        } else {

            // Win condition for workers that are moving with an effects.
            if ((turnController.getPhaseType().equals(PhaseType.YOUR_MOVE) ||
                    turnController.getPhaseType().equals(PhaseType.YOUR_MOVE_AFTER))
                    && null != positionApply
                    && winConditions(positionApply)) {
                win();
            } else {
                turnController.nextPhase();
            }
        }
    }

    /**
     * Check the win condition of the active Worker.
     *
     * @param destination destination of the Worker.
     * @return {@code true} if Player wins {@code false} otherwise.
     */
    private boolean winConditions(Position destination) {
        int origLevel = turnController.getActiveWorker().getHistory().getMoveLevel();
        int destLevel = game.getSpaceLevel(destination);
        return 2 == origLevel && 3 == destLevel;
    }

    /**
     * If Player want to active his Effect then prepare It.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void prepareEffect(PrepareEffectMessage receivedMessage) {
        if (receivedMessage.isEnableEffect()) {
            Player player = game.getPlayerByNickname(turnController.getActivePlayer());
            Effect effect = player.getGod().getEffectByType(turnController.getPhaseType());
            effect.prepare(turnController.getActiveWorker());

        } else {
            turnController.resumePhase();
        }
    }


    /**
     * Sets Active Worker and calls the Move Phase.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void pickWorkerHandler(Message receivedMessage) {

        Player player = game.getPlayerByNickname(receivedMessage.getNickname());
        Worker activeWorker = player.getWorkerByPosition(((PositionMessage) receivedMessage).getPositionList().get(0));
        turnController.setActiveWorker(activeWorker);

        turnController.movePhase(false);
    }

    /**
     * Calls Move method in order to Move on the selected Space.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void moveHandler(PositionMessage receivedMessage) {
        Position destination = receivedMessage.getPositionList().get(0);

        game.moveWorker(turnController.getActiveWorker(), destination);

        // Win condition:
        if (winConditions(destination)) {
            win();
        } else {
            // CHECK EFFECT YOUR_MOVE_AFTER
            turnController.setPhaseType(PhaseType.YOUR_MOVE_AFTER);

            if (!launchEffect()) {
                turnController.nextPhase();
            }
        }
    }

    /**
     * Calls Build method in order to Build on the selected Space.
     *
     * @param receivedMessage Message from Active Player.
     */
    private void buildHandler(PositionMessage receivedMessage) {
        Position buildOnPosition = receivedMessage.getPositionList().get(0);
        game.buildBlock(turnController.getActiveWorker(), buildOnPosition);

        // CHECK EFFECT YOUR_BUILD_AFTER
        turnController.setPhaseType(PhaseType.YOUR_BUILD_AFTER);

        if (!launchEffect()) {
            turnController.nextPhase();
        }
    }


    /**
     * Initializes the game after all Clients are connected and all Gods, Workers and Colors are setted up.
     */
    private void startGame() {
        setGameState(GameState.IN_GAME);
        broadcastGenericMessage("Game Started!");

        turnController.newTurn();
    }

    /**
     * Set the State of the current Game.
     *
     * @param gameState State of the current Game.
     */
    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Broadcasts the winner, disconnects all clients and resets whole game.
     */
    public void win() {
        broadcastWinMessage(turnController.getActivePlayer());
        //endGame();
        setGameState(GameState.END);
    }


    /**
     * Broadcasts a message to all the clients connected.
     *
     * @param winningPlayer the nickname of the winning player.
     */
    private void broadcastWinMessage(String winningPlayer) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showWinMessage(winningPlayer);
        }
    }

    /**
     * Reset the Game Instance and re-initialize GameController Class.
     */
    public void endGame() {
        Game.resetInstance();

        StorageData storageData = new StorageData();
        storageData.delete();

        initGameController();
    }

    // INIT METHODS:

    /**
     * Ask to current Player if want to enable his Effect or bypass the question and apply Effect.
     *
     * @return {@code true} if everything is done, {@code false} otherwise.
     */
    private boolean launchEffect() {
        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        if (turnController.checkEffectPhase(turnController.getPhaseType()) && turnController.requireEffect()) {
            Effect effect = player.getGod().getEffectByType(turnController.getPhaseType());
            if (effect.isUserConfirmNeeded()) {
                VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
                virtualView.askEnableEffect(false);
            } else {
                effect.apply(turnController.getActiveWorker(), null);
                turnController.setAppliedEffect(effect);
                turnController.nextPhase();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handles the login of a player. If the player is new, his VirtualView is saved, otherwise it's discarded
     * and the player is notified.
     * If it's the first Player then ask number of Players he wants, add Player to the Game otherwise change the GameState.
     *
     * @param nickname    the nickname of the player.
     * @param virtualView the virtualview of the player.
     */
    public void loginHandler(String nickname, VirtualView virtualView) {

        if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));

            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            virtualView.askPlayersNumber();

        } else if (virtualViewMap.size() < game.getChosenPlayersNumber()) {
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);

            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged

                // check saved matches.
                StorageData storageData = new StorageData();
                GameController savedGameController = storageData.restore();
                if (savedGameController != null &&
                        savedGameController.getTurnController().getNicknameQueue().containsAll(game.getPlayersNicknames())) {
                    restoreControllers(savedGameController);
                    broadcastRestoreMessages();
                    Server.LOGGER.info("Saved Match restored.");
                    turnController.newTurn();
                } else {
                    initGame();
                }
            }
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }
    }

    /**
     * Restore Message for all connected Clients.
     */
    private void broadcastRestoreMessages() {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showBoard(game.getReducedSpaceBoard());
        }

        for (VirtualView vv : virtualViewMap.values()) {

            vv.showMatchInfo(turnController.getNicknameQueue(), getActiveGods(), getPlayersColors(), turnController.getActivePlayer());
        }
    }

    /**
     * Restore Controllers from file.
     *
     * @param savedGameController Controller from file.
     */
    private void restoreControllers(GameController savedGameController) {
        Game restoredInstanceGame = savedGameController.game;
        Board restoredBoard = savedGameController.game.getBoard();
        List<Player> restoredPlayers = savedGameController.game.getPlayers();
        List<God> restoredGods = savedGameController.game.getGods();
        int restoredChoosenPlayerNumber = savedGameController.game.getChosenPlayersNumber();
        this.game.restoreGame(restoredInstanceGame, restoredBoard, restoredPlayers, restoredGods, restoredChoosenPlayerNumber);

        this.turnController = savedGameController.turnController;
        this.gameState = savedGameController.gameState;

        // set this gameController as Observer of all effects of all gods of all players.
        for (int i = 0; i < game.getNumCurrentPlayers(); i++) {
            game.getPlayerByNickname(turnController.getNicknameQueue().get(i)).getGod().addObserverToAllEffects(this);
        }

        inputController = new InputController(this.virtualViewMap, this);
        turnController.setVirtualViewMap(this.virtualViewMap);
    }

    /**
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the gods
     */
    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this);
        broadcastGenericMessage("All Players are connected. " + turnController.getActivePlayer()
                + " is choosing " + game.getChosenPlayersNumber() + " Gods . . .");

        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
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

            broadcastGenericMessage("All Gods received from " + turnController.getActivePlayer()
                    + ". Waiting for other players to pick...");

            askGodToNextPlayer();

        } else { // else receivedMessage contains only 1 god
            God god = game.getGodByName(receivedMessage.getGodList().get(0).getName());
            god.addObserverToAllEffects(this);
            game.getPlayerByNickname(receivedMessage.getNickname()).setGod(god);
            availableGods.remove(receivedMessage.getGodList().get(0));

            if (!availableGods.isEmpty()) {
                virtualView.showGenericMessage("You chose your God. Please wait for the other players to pick!");
                broadcastGenericMessage("The player " + turnController.getActivePlayer() + " picked his god.", turnController.getActivePlayer());
                askGodToNextPlayer();
            } else {
                // the last one who pick his god is the challenger, who has to choose the first player.

                virtualView.askFirstPlayer(turnController.getNicknameQueue(), getActiveGods());
            }
        }
    }


    /**
     * Handles the Challenger's choice for the first player.
     *
     * @param firstPlayerNick first player choosen by Challenger.
     */
    private void pickFirstPlayerHandler(String firstPlayerNick) {

        turnController.setActivePlayer(firstPlayerNick);

        broadcastGenericMessage("The player " + turnController.getActivePlayer() + " is choosing his color...", turnController.getActivePlayer());
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.showGenericMessage("It's your turn. Please pick your color.");
        virtualView.askInitWorkerColor(availableColors);
    }


    /**
     * Ask to pick a God to the next Player.
     */
    private void askGodToNextPlayer() {
        // ask god to the next player
        turnController.next();
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        virtualView.askGod(availableGods, 1); // Only 1 god requested to client.
    }

    /**
     * Ask to pick the 2 Workers' Positions to a Player.
     *
     * @param nickname nickname of the current Player.
     */
    private void askWorkersPositions(String nickname) {
        VirtualView virtualView = virtualViewMap.get(nickname);

        virtualView.showBoard(game.getReducedSpaceBoard());
        virtualView.askInitWorkersPositions(game.getFreePositions());
        virtualView.showMatchInfo(turnController.getNicknameQueue(), getActiveGods(), getPlayersColors(), turnController.getActivePlayer());

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
            VirtualView vv = virtualViewMap.get(turnController.getActivePlayer());
            vv.askInitWorkerColor(availableColors);
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

    /**
     * @return a List of available Gods.
     */
    public List<ReducedGod> getAvailableGods() {
        return availableGods;
    }

    /**
     * @return a List of available Colors.
     */
    public List<Color> getAvailableColors() {
        return availableColors;
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


    /**
     * Returns the list of colors of players in nicknameQueue.
     * @return a list of colors.
     */
    public List<Color> getPlayersColors() {
        List<Color> colors = new ArrayList<>();

        for(String n : turnController.getNicknameQueue()) {
            Player p = game.getPlayerByNickname(n);
            Position pos = p.getWorkersPositions().get(0);
            Worker w = p.getWorkerByPosition(pos);
            colors.add(w.getColor());
        }

        return colors;
    }


    /**
     * Returns a List of Gods sorted with their own players.
     *
     * @return a List of Gods picked by the Challenger.
     */
    public List<ReducedGod> getActiveGods() {

        List<ReducedGod> gods = new ArrayList<>();
        for (String s : turnController.getNicknameQueue()) {
            gods.add(new ReducedGod(game.getPlayerByNickname(s).getGod()));
        }

        return gods;
    }

    //********** VIRTUAL VIEWS METHODS **************//

    /**
     * Adds a Player VirtualView to the controller if the first player max_players is not exceeded.
     * Then adds a controller observer to the view.
     * Adds the VirtualView to the game and board model observers.
     *
     * @param nickname    the player nickname to identify his associated VirtualView.
     * @param virtualView the VirtualView to be added.
     */
    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
        game.addObserver(virtualView);
        game.getBoard().addObserver(virtualView);
    }


    /**
     * @return Virtual View Map.
     */
    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }


    /**
     * Removes a VirtualView from the controller.
     *
     * @param nickname      the nickname of the VirtualView associated.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     */
    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);

        game.removeObserver(vv);
        game.getBoard().removeObserver(vv);
        game.removePlayerByNickname(nickname, notifyEnabled);
    }

    /**
     * Sends a Message which contains Game Information to all players but the one specified in the second argument.
     *
     * @param messageToNotify Message to send.
     * @param excludeNickname name of the player to be excluded from the broadcast.
     */
    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    /**
     * Sends a Message which contains Game Information to every {@link Player} in Game.
     *
     * @param messageToNotify Message to send.
     */
    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
        }
    }

    /**
     * Sends a broadcast disconnection message.
     *
     * @param nicknameDisconnected user who had a connection drop.
     * @param text                 generic text.
     */
    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }

    /**
     * Receives an update message from the effect model.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        switch (message.getMessageType()) {
            case MOVE_FX:
                virtualView.askMoveFx(((PositionMessage) message).getPositionList());
                break;
            case BUILD_FX:
                virtualView.askBuildFx(((PositionMessage) message).getPositionList());
                break;
            case WIN_FX:
                win();
                break;
            case ERROR:
                ErrorMessage errMsg = (ErrorMessage) message;
                Server.LOGGER.warning(errMsg.getError());
                break;
            default:
                Server.LOGGER.warning("Invalid effect request!");
                break;
        }

    }

    /**
     * Verifies the nickname through the InputController.
     *
     * @param nickname the nickname to be checked.
     * @param view     the view of the player who is logging in.
     * @return see {@link InputController#checkLoginNickname(String, View)}
     */
    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
    }

    /**
     * Checks if the game is already started, then no more players can connect.
     *
     * @return {@code true} if the game isn't started yet, {@code false} otherwise.
     */
    public boolean isGameStarted() {
        return this.gameState != GameState.LOGIN;
    }

    /**
     * Checks if the game is already finished.
     *
     * @return {@code true} if the game is finished, {@code false} otherwise.
     */
    public boolean isGameFinished() { return this.gameState == GameState.END;}

    /**
     * Return Turn Controller of the Game.
     *
     * @return Turn Controller of the Game.
     */
    public TurnController getTurnController() {
        return turnController;
    }
}
