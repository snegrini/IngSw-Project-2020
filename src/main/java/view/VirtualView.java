package view;

import model.ReducedGod;
import model.board.Position;
import model.board.ReducedSpace;
import model.enumerations.Color;
import network.message.*;
import network.server.ClientHandler;
import observer.Observer;

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
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful) {
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

    /**
     * Sends a message to the client by using the associated client handler.
     *
     * @param message the message to be sent.
     */
    public void sendMessage(Message message) {
        clientHandler.sendMessage(message);
    }
}
