package controller;

import model.ReducedGod;
import model.board.Position;
import model.enumerations.Color;
import network.client.Client;
import network.client.SocketClient;
import network.message.*;
import observer.Observer;
import view.View;
import observer.ViewObserver;

import java.io.IOException;
import java.util.ArrayList;
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
            case GODLIST:
                GodListMessage godListMessage = (GodListMessage) message;
                view.askGod(godListMessage.getGodList(), godListMessage.getRequest());
                break;
            case INIT_WORKERSPOSITIONS:
                WorkersPositionsMessage workersPositionsMessage = (WorkersPositionsMessage) message;
                view.askInitWorkersPositions(workersPositionsMessage.getPositionList());
                break;
            case INIT_COLORS:

                break;
            case GENERIC_ERROR_MESSAGE:
                view.showGenericErrorMessage(message.toString()); // TODO check
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
        client.sendMessage(new ColorsMessage(this.nickname, List.of(color)));
    }

    @Override
    public void onUpdateGod(List<ReducedGod> gods) {
        client.sendMessage(new GodListMessage(this.nickname, gods, 0));
    }

    @Override
    public void onUpdateWorkerToMove(Position position) {

    }

    @Override
    public void onUpdateInitWorkerPosition(List<Position> positions) {

    }

    @Override
    public void onUpdateWorkerPosition(Position dest, Position orig) {

    }

    @Override
    public void onUpdateBuild(Position position) {

    }

}
