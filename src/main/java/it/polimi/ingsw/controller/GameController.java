package it.polimi.ingsw.controller;

import it.polimi.ingsw.ServerApp;
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
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.persistence.Persistence;
import it.polimi.ingsw.persistence.StorageData;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.network.message.MessageType.PLAYERNUMBER_REPLY;

public class GameController implements Observer, Serializable {

    private Game game;
    private transient Map<String, VirtualView> virtualViewMap;

    private GameState gameState;
    private TurnController turnController;
    private InputController inputController;

    private transient List<ReducedGod> availableGods;
    private transient List<Color> availableColors;

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
            // TODO show exception.
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
                // TODO check input
                pickFirstPlayerHandler(((UsersInfoMessage) receivedMessage).getActivePlayerNickname());
                break;
            case INIT_COLORS:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    colorHandler((ColorsMessage) receivedMessage);
                }
                break;
            case INIT_WORKERSPOSITIONS:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    workerPositionsHandler((PositionMessage) receivedMessage, virtualView);
                }
                break;

            default:
                // TODO show exception
                break;
        }
    }

    /**
     * Switch on Game Messages' Types.
     *
     * @param receivedMessage Message from Active Player.
     * @param virtualView     Virtual View the Active PLayer.
     */
    private void inGameState(Message receivedMessage, VirtualView virtualView) {
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
                // TODO show exception
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
        //effect.clear(turnController.getActiveWorker());
        turnController.setAppliedEffect(effect);

        // TODO FIX hardcode Prometheus
        if (player.getGod().getName().equals("Prometheus")) {
            turnController.resumePhase();
        } else {
            turnController.nextPhase();
        }

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
     * Set Active Worker and calls the Move Phase.
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

        int origLevel = game.getSpaceLevel(turnController.getActiveWorker().getPosition());
        int destLevel = game.getSpaceLevel(destination);

        game.moveWorker(turnController.getActiveWorker(), destination);

        // Win condition:
        if (origLevel == 2 && destLevel == 3) {
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
     * Initialize the game after all Clients are connected and all Gods, Workers and Colors are setted up.
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
     * Broadcast the winner, disconnect all clients and reset whole game.
     */
    private void win() {
        broadcastWinMessage(turnController.getActivePlayer());
        disconnectAllClients();
        endGame();
    }

    private void broadcastWinMessage(String activePlayer) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showWinMessage(activePlayer);
        }
    }

    /**
     * Reset the Game Instance and re-initialize GameController Class.
     */
    public void endGame() {
        Game.resetInstance();
        initGameController();
    }

    // INIT METHODS:

    /**
     * Ask to current Player if want to enable his Effect or bypass the question and apply Effect.
     *
     * @return {@code true} if everything is done {@code false} otherwise.
     */
    private Boolean launchEffect() {
        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        if (turnController.checkEffectPhase(turnController.getPhaseType()) && turnController.requireEffect()) {
            Effect effect = player.getGod().getEffectByType(turnController.getPhaseType());
            if (effect.isUserConfirmNeeded()) {
                VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
                virtualView.askEnableEffect(false);
            } else {
                effect.apply(turnController.getActiveWorker(), null);
                //effect.clear(turnController.getActiveWorker());
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
                if (storageData.restore() != null) {
                    GameController savedGameController = storageData.restore();
                    if (savedGameController.getTurnController().getNicknameQueue().equals(game.getPlayersNicknames())) {
                        restoreControllers(savedGameController);
                        broadcastRestoreMessages();
                        turnController.newTurn();
                    }
                } else {
                    initGame();
                }
            }
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }
    }

    private void broadcastRestoreMessages() {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showBoard(game.getReducedSpaceBoard());
        }

        for (VirtualView vv : virtualViewMap.values()) {
            vv.showMatchInfo(turnController.getNicknameQueue(), getActiveGods(), turnController.getActivePlayer());

        }
    }


    private void restoreControllers(GameController savedGameController) {



       // this.game = savedGameController.game;
        Game restoredInstanceGame = savedGameController.game;
        Board restoredBoard = savedGameController.game.getBoard();
        List<Player> restoredPlayers = savedGameController.game.getPlayers();
        List<God> restoredGods = savedGameController.game.getGods();
        int restoredChoosenPlayerNumber = savedGameController.game.getChosenPlayersNumber();
        this.game.restoreGame(restoredInstanceGame, restoredBoard,restoredPlayers,restoredGods, restoredChoosenPlayerNumber);

        this.turnController = savedGameController.turnController;
        this.gameState = savedGameController.gameState;

        // set this gameController as Observer of all effects of all gods of all players.
        for (int i = 0; i<game.getNumCurrentPlayers(); i++) {
            game.getPlayerByNickname(turnController.getNicknameQueue().get(i)).getGod().addObserverToAllEffects(this);
        }

        inputController = new InputController(this.virtualViewMap, this);
        turnController.setVirtualViewMap(this.virtualViewMap);
        inputController.setTurnController(this.turnController);


    }

    /**
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the gods
     */
    private void initGame() {
        setGameState(GameState.INIT);

        turnController = new TurnController(virtualViewMap, this);
        inputController.setTurnController(turnController);
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
                    + ". Waiting for other players to pick . . .");

            askGodToNextPlayer();

        } else { // else receivedMessage contains only 1 god
            God god = game.getGodByName(receivedMessage.getGodList().get(0).getName());
            god.addObserverToAllEffects(this);
            game.getPlayerByNickname(receivedMessage.getNickname()).setGod(god);
            availableGods.remove(receivedMessage.getGodList().get(0));

            if (!availableGods.isEmpty()) {
                broadcastGenericMessage("God received from " + turnController.getActivePlayer()
                        + ". Waiting for other players to pick . . .");
                askGodToNextPlayer();
            } else {
                // the last one who pick his god is the challenger, so He have to choose the first player.

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

        broadcastGenericMessage("Initializing " + turnController.getActivePlayer() + ". . .");
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
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
        virtualView.showMatchInfo(turnController.getNicknameQueue(), getActiveGods(), turnController.getActivePlayer());

    }

    /**
     * Assign 2 positions to 2 workers of the player
     *
     * @param receivedMessage message from client
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

        if (availableColors.size() != Game.MAX_PLAYERS - game.getChosenPlayersNumber()) {
            turnController.next();
            virtualView = virtualViewMap.get(turnController.getActivePlayer());
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
     * @return Virtual View Map
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
        game.removePlayerByNickname(nickname, notifyEnabled);
        game.removeObserver(vv);
        game.getBoard().removeObserver(vv);
    }

    /**
     * Sends a Message which contains Game Information to every Players in Game.
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
     * Disconnect all connected clients
     */
    private void disconnectAllClients() {
        int mapSize = virtualViewMap.size();
        for (int i = 0; i < mapSize - 1; i++)
            virtualViewMap.get(turnController.getNicknameQueue().get(i)).getClientHandler().disconnect();
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

    public TurnController getTurnController() {
        return turnController;
    }
}
