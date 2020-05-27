package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.Effect;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.GameState;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class GameController implements Observer {

    private Game game;
    private Map<String, VirtualView> virtualViewMap;

    private GameState gameState;
    private List<ReducedGod> availableGods;
    private List<Color> availableColors;



    private List<ReducedGod> activeGods;


    private TurnController turnController;
    private InputController inputController;

    public GameController() {
        initGameController();
    }

    public void initGameController() {
        this.game = Game.getInstance();
        this.availableColors = getColorList();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        this.gameState = GameState.LOGIN; // Initialize Game State.
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
        switch (receivedMessage.getMessageType()) {
            case PLAYERNUMBER_REPLY:
                if (inputController.verifyReceivedData(receivedMessage)) {
                    game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
                    broadcastGenericMessage("Waiting for other Players . . .");
                }
                break;
            default:
                // TODO show exception
                break;
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
        effect.clear(turnController.getActiveWorker());

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
        gameState = GameState.IN_GAME;
        broadcastGenericMessage("Game Started!");

        turnController.newTurn();
    }

    private void win() {
        broadcastGenericMessage(turnController.getActivePlayer() + " wins! Game Finished!");

        disconnectAllClients();

        // TODO end game, prepare server for a new game. Set server on listen for the first client.
        Game.resetInstance();
        initGameController();
    }

    void endGame() {
        // TODO send message to all players, close connections
    }

    // INIT METHODS:

    private Boolean launchEffect() {
        Player player = game.getPlayerByNickname(turnController.getActivePlayer());
        if (turnController.checkEffectPhase(turnController.getPhaseType()) && turnController.requireEffect()) {
            Effect effect = player.getGod().getEffectByType(turnController.getPhaseType());
            if (effect.isUserConfirmNeeded()) {
                VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
                virtualView.askEnableEffect();
            } else {
                effect.apply(turnController.getActiveWorker(), null);
                effect.clear(turnController.getActiveWorker());
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

            // TODO maybe removable --
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            // --

            virtualView.askPlayersNumber();
        } else if (virtualViewMap.size() < game.getChosenPlayersNumber()) {
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);

            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                initGame();
            }
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }
    }


    /**
     * Change gameState into INIT. Initialize TurnController and asks a player to pick the gods
     */
    private void initGame() {
        gameState = GameState.INIT;
        turnController = new TurnController(virtualViewMap);
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
            activeGods = new ArrayList<>(receivedMessage.getGodList());
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
                // the last one who pick his god is the first one player of every round.

                broadcastGenericMessage("Initializing " + turnController.getActivePlayer()
                        + ". . .");
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
        virtualView.showMatchInfo(turnController.getNicknameQueue(), activeGods, turnController.getActivePlayer());
        virtualView.askInitWorkersPositions(game.getFreePositions());
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

    // TODO test?

    /**
     * @return a List of available Gods.
     */
    public List<ReducedGod> getAvailableGods() {
        return availableGods;
    }

    // TODO test?

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

    public List<ReducedGod> getActiveGods() {
        return activeGods;
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
     * @param nickname the nickname of the VirtualView associated.
     */
    public void removeVirtualView(String nickname) {
        VirtualView vv = virtualViewMap.remove(nickname);
        game.removePlayerByNickname(nickname);
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
     * @return see {@link #checkLoginNickname(String, View)}
     */
    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
    }
}
