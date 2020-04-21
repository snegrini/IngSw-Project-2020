package controller;

import model.ReducedGod;
import model.enumerations.Color;
import network.client.Client;
import network.client.SocketClient;
import network.message.GodList;
import network.message.LoginRequest;
import network.message.Message;
import network.message.PlayerNumberReply;
import observer.Observer;
import view.View;
import observer.ViewObserver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientController implements ViewObserver, Observer {

    private View view;
    private Client client;
    private String nickname;

    public ClientController(View view) {
        this.view = view;
    }

    /**
     * Takes action based on the message type received from the server.
     *
     * @param message the message received from the server.
     */
    @Override
    public void update(Message message) {
        switch (message.getMessageType()) {
            case PLAYERNUMBER_REQUEST:
                view.askPlayersNumber();
                break;
            default: // Should never reach this condition
                break;
        }
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
        } catch (IOException e) {
            // TODO show error in view and return the old view.
            System.out.println("could not contact server");
        }
        view.askNickname();
    }

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));
    }

    @Override
    public void onUpdateWorkersColor(Color color) {

    }

    @Override
    public void onUpdateGod(ReducedGod god) {
        client.sendMessage(new GodList(this.nickname, List.of(god)));
    }

    @Override
    public void onUpdateWorkerToMove(int chosenRow, int chosenColumn) {

    }

    @Override
    public void onUpdateWorkerPosition(int chosenRow, int chosenColumn) {

    }

    @Override
    public void onUpdateBuild(int chosenRow, int chosenColumn) {

    }

}
