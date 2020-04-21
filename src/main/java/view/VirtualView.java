package view;

import model.God;
import model.ReducedGod;
import model.board.Position;
import model.enumerations.Color;
import model.player.Worker;
import network.message.GodList;
import network.message.LoginReply;
import network.message.Message;
import network.message.PlayerNumberRequest;
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
    public void askWorkerToMove(List<Position> positions) {

    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askWorkersColor(List<Color> colors) {

    }

    @Override
    public void askGod(List<ReducedGod> gods) {
        clientHandler.sendMessage(new GodList("server", gods));
    }

    @Override
    public String showBoard() {
        return null;
    }

    @Override
    public void askNewBuildingPosition(List<Position> positions) {

    }

    @Override
    public void askNewPosition(List<Position> positions) {

    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
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
