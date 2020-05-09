package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientController implements ViewObserver, Observer {

    private final View view;
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
            case BOARD:
                BoardMessage boardMessage = (BoardMessage) message;
                view.showBoard(boardMessage.getBoard());
                break;
            case BUILD:
                view.askNewBuildingPosition(((PositionMessage) message).getPositionList());
                break;
            case INIT_COLORS:
                view.askInitWorkerColor(((ColorsMessage) message).getColorList());
                break;
            case GENERIC_MESSAGE:
                view.showGenericMessage(((GenericMessage) message).getMessage());
                break;
            case GODLIST:
                GodListMessage godListMessage = (GodListMessage) message;
                view.askGod(godListMessage.getGodList(), godListMessage.getRequest());
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname);
                break;
            case LOGIN_REQUEST: // Should never be here.
                break;
            case MOVE:
                view.askMove(((PositionMessage) message).getPositionList());
                break;
            case PICK_MOVING_WORKER:
                view.askMovingWorker(((PositionMessage) message).getPositionList());
                break;
            case PLAYERNUMBER_REPLY: // Should never be here.
                break;
            case PLAYERNUMBER_REQUEST:
                view.askPlayersNumber();
                break;
            case INIT_WORKERSPOSITIONS:
                view.askInitWorkersPositions(((PositionMessage) message).getPositionList());
                break;
            case ERROR:
                view.showError(((ErrorMessage) message).getError());
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
            client.enablePinger(true);
        } catch (IOException e) {
            view.showLoginResult(false, false, this.nickname);
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
    public void onUpdatePickMovingWorker(Position position) {
        client.sendMessage(new PositionMessage(this.nickname, MessageType.PICK_MOVING_WORKER, List.of(position)));
    }

    @Override
    public void onUpdateInitWorkerPosition(List<Position> positions) {
        client.sendMessage(new PositionMessage(this.nickname, MessageType.INIT_WORKERSPOSITIONS, positions));
    }

    @Override
    public void onUpdateMove(Position destination) {
        client.sendMessage(new PositionMessage(this.nickname, MessageType.MOVE, List.of(destination)));
    }

    @Override
    public void onUpdateBuild(Position position) {
        client.sendMessage(new PositionMessage(this.nickname, MessageType.BUILD, List.of(position)));
    }

    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    public static boolean isValidPort(int port) {
        return port >= 1 && port <= 65565;
    }

}
