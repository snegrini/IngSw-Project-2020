package view;

import model.God;
import model.enumerations.Color;
import model.player.Worker;
import network.message.Message;
import network.message.PlayerNumberRequest;
import network.server.ClientHandler;
import observer.Observer;

import java.util.List;

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
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void askWorkersColor(List<Color> colors) {

    }


    /**
     * Receives an update from the model. A proper action is taken based on the type of the received message.
     *
     * @param message the update message.
     */
    @Override
    public void update(Message message) {
        // TODO
    }

    /**
     * Sends a message to the client by using the associated client handler.
     *
     * @param message the message to be sent.
     */
    public void sendMessage(Message message) {
        clientHandler.sendMessage(message);
    }

    public String showBoard() {
        // TODO
        return null;
    }

    public void askGod(List<God> gods) {

    }

    public void askWorkerToMove(List<Worker> workers) {

    }

    public void askNewBuildingPosition(Worker worker) {

    }

    public void askNewPosition(Worker worker) {

    }

    public void initializeBoard() {

    }
}

