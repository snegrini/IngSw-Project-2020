package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.ReducedSpace;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observer;

import java.util.List;

/**
 * Hides the it.polimi.ingsw.network implementation from the it.polimi.ingsw.controller.
 * The it.polimi.ingsw.controller calls methods from this class as if it was a normal it.polimi.ingsw.view.
 * Instead, a it.polimi.ingsw.network protocol is used to communicate with the real it.polimi.ingsw.view on the client side.
 */
public class VirtualView extends View implements Observer {
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }


    @Override
    public void init() {

    }

    @Override
    public void askServerInfo() {
    }

    @Override
    public void askNickname() {
    }

    @Override
    public void askMovingWorker(List<Position> positionList) {
        clientHandler.sendMessage(new PositionMessage("server", MessageType.PICK_MOVING_WORKER, positionList));
    }

    @Override
    public void askMove(List<Position> positionList) {
        clientHandler.sendMessage(new PositionMessage("server", MessageType.MOVE, positionList));
    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        clientHandler.sendMessage(new ColorsMessage("server", colors));
    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage("server", MessageType.INIT_WORKERSPOSITIONS, positions));
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        clientHandler.sendMessage(new GodListMessage("server", gods, request));
    }


    @Override
    public void askNewBuildingPosition(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage("server", MessageType.BUILD, positions));
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
    public void showBoard(ReducedSpace[][] spaces) {
        clientHandler.sendMessage(new BoardMessage("server", MessageType.BOARD, spaces));
    }


    /**
     * Receives an update message from the it.polimi.ingsw.model.
     * The message is sent over the it.polimi.ingsw.network to the client.
     * The proper action based on the message type will be taken by the real it.polimi.ingsw.view on the client.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Sends a message to the client by using the associated client handler.
     *
     * @param message the message to be sent.
     */
    public void sendMessage(Message message) {
        clientHandler.sendMessage(message);
    }
}
