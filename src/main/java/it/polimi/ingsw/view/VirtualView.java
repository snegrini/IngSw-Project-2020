package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observer;

import java.util.List;

/**
 * Hides the network implementation from the controller.
 * The controller calls methods from this class as if it was a normal view.
 * Instead, a network protocol is used to communicate with the real view on the client side.
 */
public class VirtualView extends View implements Observer {
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @Override
    public void askNickname() {
        // TODO use to re-ask a nickname if it is not unique.
    }

    @Override
    public void askMovingWorker(List<Position> positionList) {
        clientHandler.sendMessage(new PositionMessage(Game.SERVER_NICKNAME, MessageType.PICK_MOVING_WORKER, positionList));
    }

    @Override
    public void askMove(List<Position> positionList) {
        clientHandler.sendMessage(new PositionMessage(Game.SERVER_NICKNAME, MessageType.MOVE, positionList));
    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        clientHandler.sendMessage(new ColorsMessage(Game.SERVER_NICKNAME, colors));
    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage(Game.SERVER_NICKNAME, MessageType.INIT_WORKERSPOSITIONS, positions));
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        clientHandler.sendMessage(new GodListMessage(Game.SERVER_NICKNAME, gods, request));
    }


    @Override
    public void askNewBuildingPosition(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage(Game.SERVER_NICKNAME, MessageType.BUILD, positions));
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void showError(String error) {
        clientHandler.sendMessage(new ErrorMessage(Game.SERVER_NICKNAME, error));
    }

    @Override
    public void showBoard(ReducedSpace[][] spaces) {
        clientHandler.sendMessage(new BoardMessage(Game.SERVER_NICKNAME, MessageType.BOARD, spaces));
    }


    /**
     * Receives an update message from the model.
     * The message is sent over the network to the client.
     * The proper action based on the message type will be taken by the real view on the client.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }
}
