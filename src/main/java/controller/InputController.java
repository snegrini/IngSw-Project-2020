package controller;

import model.Game;
import model.ReducedGod;
import model.enumerations.Color;
import network.message.*;
import view.VirtualView;

import java.util.Map;

public class InputController {

    private Game game;
    private Map<String, VirtualView> virtualViews;
    private GameController gameController;

    public InputController(Map<String, VirtualView> virtualViews, GameController gameController) {
        this.game = Game.getInstance();
        this.virtualViews = virtualViews;
        this.gameController = gameController;
    }

    public boolean check(Message message) {

        switch (message.getMessageType()) {
            case BOARD: // server doesn't never receive a BoardMessage.
                return false;
            case BUILD:
                return buildCheck(message);
            case INIT_COLORS:
                return colorCheck(message);
            case GENERIC_MESSAGE: // server doesn't receive a GenericErrorMessage.
                return false;
            case GODLIST:
                return godListCheck(message);
            case LOGIN_REPLY: // server doesn't receive a GenericErrorMessage.
                return false;
            case LOGIN_REQUEST:
                return loginRequestCheck(message);
            case MOVE:
                return moveCheck(message);
            case PICK_MOVING_WORKER:
                return pickMovingCheck(message);
            case PICK_BUILDING_WORKER:
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

    private boolean pickMovingCheck(Message message) {
        // TODO
        return true;
    }

    private boolean workerPositionsCheck(Message message) {

        if (((PositionMessage) message).getPositionList().size() == 2) {
            // if both positions are not equals each other and are free return true.
            if (!(((PositionMessage) message).getPositionList().get(0)
                    .equals(((PositionMessage) message).getPositionList().get(1))) &&
                    game.getBoard().arePositionsFree(((PositionMessage) message).getPositionList())) {
                return true;
            } else {
                VirtualView virtualView = virtualViews.get(message.getNickname());
                // Avoid to show board.
                virtualView.showGenericMessage("Positions are not free!");
                virtualView.askInitWorkersPositions(game.getBoard().getFreePositions());
                return false;
            }
        } else {
            VirtualView virtualView = virtualViews.get(message.getNickname());
            // Avoid to show board.
            virtualView.showGenericMessage("Positions must be 2!");
            virtualView.askInitWorkersPositions(game.getBoard().getFreePositions());
            return false;
        }
    }

    private boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;

        if (playerNumberReply.getPlayerNumber() < 4 && playerNumberReply.getPlayerNumber() > 1) {
            return true;
        } else {
            VirtualView virtualView = virtualViews.get(message.getNickname());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    private boolean moveCheck(Message message) {
        // TODO
        if (!((PositionMessage) message).getPositionList().isEmpty()) {
            return true;
        } else {
            // TODO Re-ask Move 'cause client provided a null destination.
            return false;
        }
    }

    private boolean loginRequestCheck(Message message) {

        VirtualView virtualView = virtualViews.get(message.getNickname());
        String nickname = message.getNickname();

        if (nickname.equals("server") || nickname.equals("SERVER") || nickname.equals("Server")) {
            virtualView.showGenericMessage("Forbidden name.");
            virtualView.showLoginResult(false, true);
            return false;
        } else if (game.isNicknameTaken(nickname)) {
            virtualView.showGenericMessage("Nickname already taken");
            virtualView.showLoginResult(false, true);
            return false;
        } else {
            return true;
        }
    }

    private boolean godListCheck(Message message) {

        GodListMessage godListMessage = (GodListMessage) message;

        if (godListMessage.getGodList().size() > 1) { // if is a list check that size correspond to number of players.
            if (godListMessage.getGodList().size() == game.getChosenPlayersNumber()) {
                return true;
            } else {
                VirtualView virtualView = virtualViews.get(message.getNickname());
                virtualView.askGod(game.getReduceGodList(), game.getChosenPlayersNumber());
                return false;
            }
        } else if (isInSelectedGodList(godListMessage.getGodList().get(0))) { // if is only 1 god check if had been selected
            return true;
        } else {
            VirtualView virtualView = virtualViews.get(message.getNickname());
            virtualView.askGod(gameController.getAvailableGods(), 1);
            return false;
        }
    }

    private boolean colorCheck(Message message) {

        if (((ColorsMessage) message).getColorList().size() == 1) {
            if (isInAvailableColor(((ColorsMessage) message).getColorList().get(0))) {
                return true;
            } else {
                VirtualView virtualView = virtualViews.get(message.getNickname());
                virtualView.askInitWorkerColor(gameController.getAvailableColors());
                return false;
            }
        } else {
            VirtualView virtualView = virtualViews.get(message.getNickname());
            virtualView.askInitWorkerColor(gameController.getAvailableColors());
            return false;
        }

    }

    private boolean buildCheck(Message message) {
        // TODO
        return false;
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
     * Check if god is in the selectedGodList
     *
     * @param god god picked by client
     * @return {@code true} if correct {@code false} otherwise
     */
    private boolean isInSelectedGodList(ReducedGod god) {
        for (ReducedGod g : gameController.getAvailableGods()) {
            if (g.getName().equals(god.getName()))
                return true;
        }
        return false;
    }

}
