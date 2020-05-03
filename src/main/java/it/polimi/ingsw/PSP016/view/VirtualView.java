package it.polimi.ingsw.PSP016.view;

import it.polimi.ingsw.PSP016.model.board.ReducedSpace;
import it.polimi.ingsw.PSP016.network.message.*;
import it.polimi.ingsw.PSP016.model.Game;
import it.polimi.ingsw.PSP016.model.ReducedGod;
import it.polimi.ingsw.PSP016.model.board.Position;
import it.polimi.ingsw.PSP016.model.enumerations.Color;
import it.polimi.ingsw.PSP016.network.server.ClientHandler;
import it.polimi.ingsw.PSP016.observer.Observer;

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
        clientHandler.sendMessage(new PositionMessage(Game.serverNickname, MessageType.PICK_MOVING_WORKER, positionList));
    }

    @Override
    public void askMove(List<Position> positionList) {
        clientHandler.sendMessage(new PositionMessage(Game.serverNickname, MessageType.MOVE, positionList));
    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askInitWorkerColor(List<Color> colors) {
        clientHandler.sendMessage(new ColorsMessage(Game.serverNickname, colors));
    }

    @Override
    public void askInitWorkersPositions(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage(Game.serverNickname, MessageType.INIT_WORKERSPOSITIONS, positions));
    }

    @Override
    public void askGod(List<ReducedGod> gods, int request) {
        clientHandler.sendMessage(new GodListMessage(Game.serverNickname, gods, request));
    }


    @Override
    public void askNewBuildingPosition(List<Position> positions) {
        clientHandler.sendMessage(new PositionMessage(Game.serverNickname, MessageType.BUILD, positions));
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
        clientHandler.sendMessage(new BoardMessage(Game.serverNickname, MessageType.BOARD, spaces));
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
