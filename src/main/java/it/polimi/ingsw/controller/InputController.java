package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This Class verifies that all messages sent by client contain valid information.
 */
public class InputController implements Serializable {

    private static final long serialVersionUID = 7413156215358698632L;

    private final Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private final GameController gameController;

    /**
     * Constructor of the Input Controller Class.
     *
     * @param virtualViewMap Virtual View Map.
     * @param gameController Game Controller.
     */
    public InputController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    /**
     * Verify data sent by client to server.
     *
     * @param message Message from Client.
     * @return {code @true} if Message contains valid data {@code false} otherwise.
     */
    public boolean verifyReceivedData(Message message) {

        switch (message.getMessageType()) {
            case BOARD: // server doesn't never receive a BOARD.
                return false;
            case BUILD:
                return buildCheck(message);
            case INIT_COLORS:
                return colorCheck(message);
            case GENERIC_MESSAGE: // server doesn't receive a GENERIC_MESSAGE.
                return false;
            case GODLIST:
                return godListCheck(message);
            case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            case MOVE:
                return moveCheck(message);
            case PICK_MOVING_WORKER:
                return pickMovingCheck(message);
            case PLAYERNUMBER_REPLY:
                return playerNumberReplyCheck(message);
            case PLAYERNUMBER_REQUEST: // server doesn't receive a GenericErrorMessage.
                return false;
            case INIT_WORKERSPOSITIONS:
                return workerPositionsCheck(message);
            default: // Never should reach this statement.
                return false;
        }

    }

    /**
     * Check if a nickname is valid or not.
     *
     * @param nickname new client's nickname.
     * @param view     view for active client.
     * @return {code @true} if it's a valid nickname {code @false} otherwise.
     */
    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase(Game.SERVER_NICKNAME)) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (game.isNicknameTaken(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    public boolean checkFirstPlayerHandler(Message message) {
        String firstPlayer = ((MatchInfoMessage) message).getActivePlayerNickname();
        if (gameController.getTurnController().getNicknameQueue().contains(firstPlayer)) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.showGenericMessage("Incorrect First Player.");
            virtualView.askFirstPlayer(gameController.getTurnController().getNicknameQueue(), gameController.getActiveGods());
            return false;
        }
    }

    /**
     * Check of Pick Moving Worker message.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid position {code @false} otherwise.
     */
    private boolean pickMovingCheck(Message message) {
        Position workerPosition = ((PositionMessage) message).getPositionList().get(0);
        String activePlayerNickname = gameController.getTurnController().getActivePlayer();
        Worker pickedWorker = game.getPlayerByNickname(activePlayerNickname).getWorkerByPosition(workerPosition);
        if (null != pickedWorker) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.showGenericMessage("You don't have a worker in this position.");
            gameController.getTurnController().pickWorker();
            return false;
        }
    }

    /**
     * Check initializing workers positions.
     *
     * @param message message from client.
     * @return {code @true} if are two valid position {code @false} otherwise.
     */
    private boolean workerPositionsCheck(Message message) {

        if (((PositionMessage) message).getPositionList().size() == 2) {
            // if both positions are not equals each other and are free return true.
            if (!(((PositionMessage) message).getPositionList().get(0)
                    .equals(((PositionMessage) message).getPositionList().get(1))) &&
                    game.arePositionsFree(((PositionMessage) message).getPositionList())) {
                return true;
            } else {
                VirtualView virtualView = virtualViewMap.get(message.getNickname());
                // Avoid to show board.
                virtualView.showGenericMessage("Positions are not free!");
                virtualView.askInitWorkersPositions(game.getFreePositions());
                return false;
            }
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            // Avoid to show board.
            virtualView.showGenericMessage("Positions must be 2!");
            virtualView.askInitWorkersPositions(game.getFreePositions());
            return false;
        }
    }

    /**
     * Check player number reply message.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid number {code @false} otherwise.
     */
    private boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;

        if (playerNumberReply.getPlayerNumber() < 4 && playerNumberReply.getPlayerNumber() > 1) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    /**
     * Check of Moving Worker message.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid position {code @false} otherwise.
     */
    private boolean moveCheck(Message message) {
        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        PositionMessage positionMessage = ((PositionMessage) message);
        Position chosenDest = positionMessage.getPositionList().get(0);
        List<Position> possibleMovePositions = gameController.getTurnController().getActiveWorker().getPossibleMoves();

        if (!positionMessage.getPositionList().isEmpty() && possibleMovePositions.contains(chosenDest)) {
            return true;
        } else {
            virtualView.showGenericMessage("You didn't provided a valid Destination. Retry.");
            virtualView.askMove(gameController.getTurnController().getActiveWorker().getPossibleMoves());
            return false;
        }
    }

    /**
     * Check God List messages.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid set of gods {code @false} otherwise.
     */
    private boolean godListCheck(Message message) {

        GodListMessage godListMessage = (GodListMessage) message;

        if (godListMessage.getGodList().size() > 1) { // if is a list check that size correspond to number of players.
            if (godListMessage.getGodList().size() == game.getChosenPlayersNumber()) {
                return true;
            } else {
                VirtualView virtualView = virtualViewMap.get(message.getNickname());
                virtualView.askGod(game.getReduceGodList(), game.getChosenPlayersNumber());
                return false;
            }
        } else if (isInSelectedGodList(godListMessage.getGodList().get(0))) { // if is only 1 god check if had been selected
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askGod(gameController.getAvailableGods(), 1);
            return false;
        }
    }

    /**
     * Check Color messages.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid color {code @false} otherwise.
     */
    private boolean colorCheck(Message message) {

        if (((ColorsMessage) message).getColorList().size() == 1) {
            if (isInAvailableColor(((ColorsMessage) message).getColorList().get(0))) {
                return true;
            } else {
                VirtualView virtualView = virtualViewMap.get(message.getNickname());
                virtualView.askInitWorkerColor(gameController.getAvailableColors());
                return false;
            }
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askInitWorkerColor(gameController.getAvailableColors());
            return false;
        }

    }

    /**
     * Check of Building Worker message.
     *
     * @param message message from client.
     * @return {code @true} if it's a valid position {code @false} otherwise.
     */
    private boolean buildCheck(Message message) {

        VirtualView virtualView = virtualViewMap.get(message.getNickname());
        PositionMessage positionMessage = ((PositionMessage) message);
        Position choosenBuild = positionMessage.getPositionList().get(0);
        List<Position> possibleBuildPositions = gameController.getTurnController().getActiveWorker().getPossibleBuilds();

        if (!positionMessage.getPositionList().isEmpty() && possibleBuildPositions.contains(choosenBuild)) {
            return true;
        } else {
            virtualView.showGenericMessage("You didn't provided a valid Position. Retry.");
            virtualView.askBuild(gameController.getTurnController().getActiveWorker().getPossibleBuilds());
            return false;
        }
    }


    /**
     * Check if color picked by client is available
     *
     * @param color color picked by client
     * @return true or false
     */
    private boolean isInAvailableColor(Color color) {
        for (Color c : gameController.getAvailableColors()) {
            if (c.equals(color))
                return true;
        }
        return false;
    }


    /**
     * Check if god is in the selectedGodList.
     *
     * @param god god picked by client.
     * @return {@code true} if correct {@code false} otherwise.
     */
    private boolean isInSelectedGodList(ReducedGod god) {
        for (ReducedGod g : gameController.getAvailableGods()) {
            if (g.getName().equals(god.getName()))
                return true;
        }
        return false;
    }

    /**
     * Check if message is sent from active player.
     *
     * @param receivedMessage message from client.
     * @return {@code true} if correct {@code false} otherwise.
     */
    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(gameController.getTurnController().getActivePlayer());
    }
}
