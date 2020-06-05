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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String nickname;

    private final ExecutorService taskQueue;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.nickname));
        }
    }

    /**
     * Sends a message to the server with the updated nickname.
     * The nickname is also stored locally for later usages.
     *
     * @param nickname the nickname to be sent.
     */
    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
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

    @Override
    public void onUpdateEnableEffect(boolean response) {
        client.sendMessage(new PrepareEffectMessage(this.nickname, response));
    }

    @Override
    public void onUpdateApplyEffect(Position dest) {
        client.sendMessage(new PositionMessage(this.nickname, MessageType.APPLY_EFFECT, List.of(dest)));
    }

    @Override
    public void onUpdateFirstPlayer(String nickname) {
        client.sendMessage(new UsersInfoMessage(this.nickname, MessageType.PICK_FIRST_PLAYER, null, null, nickname));
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
                taskQueue.execute(() -> view.showBoard(boardMessage.getBoard()));
                break;
            case BUILD:
                taskQueue.execute(() -> view.askBuild(((PositionMessage) message).getPositionList()));
                break;
            case INIT_COLORS:
                taskQueue.execute(() -> view.askInitWorkerColor(((ColorsMessage) message).getColorList()));
                break;
            case GENERIC_MESSAGE:
                taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
                break;
            case DISCONNECTION:
                DisconnectionMessage dm = (DisconnectionMessage) message;
                client.disconnect();
                view.showDisconnectionMessage(dm.getNicknameDisconnected(), dm.getMessageStr());
                break;
            case GODLIST:
                GodListMessage godListMessage = (GodListMessage) message;
                taskQueue.execute(() -> view.askGod(godListMessage.getGodList(), godListMessage.getRequest()));
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname));
                break;
            case MATCH_INFO:
                UsersInfoMessage usersInfoMessage = (UsersInfoMessage) message;
                taskQueue.execute(() -> view.showMatchInfo(
                        usersInfoMessage.getActivePlayers(),
                        usersInfoMessage.getActiveGods(),
                        usersInfoMessage.getActivePlayerNickname()
                ));
                break;
            case MOVE:
                taskQueue.execute(() -> view.askMove(((PositionMessage) message).getPositionList()));
                break;
            case PICK_MOVING_WORKER:
                taskQueue.execute(() -> view.askMovingWorker(((PositionMessage) message).getPositionList()));
                break;
            case PICK_FIRST_PLAYER:
                UsersInfoMessage playersMessage = (UsersInfoMessage) message;
                taskQueue.execute(() -> view.askFirstPlayer(playersMessage.getActivePlayers(), playersMessage.getActiveGods()));
                break;
            case PLAYERNUMBER_REQUEST:
                taskQueue.execute(view::askPlayersNumber);
                break;
            case INIT_WORKERSPOSITIONS:
                taskQueue.execute(() -> view.askInitWorkersPositions(((PositionMessage) message).getPositionList()));
                break;
            case ERROR:
                taskQueue.execute(() -> view.showErrorAndExit(((ErrorMessage) message).getError()));
                break;
            case ENABLE_EFFECT:
                taskQueue.execute(() -> view.askEnableEffect(((PrepareEffectMessage) message).isEnableEffect()));
                break;
            case MOVE_FX:
                taskQueue.execute(() -> view.askMoveFx(((PositionMessage) message).getPositionList()));
                break;
            case BUILD_FX:
                taskQueue.execute(() -> view.askBuildFx(((PositionMessage) message).getPositionList()));
                break;
            case LOBBY:
                LobbyMessage lobbyMessage = (LobbyMessage) message;
                taskQueue.execute(() -> view.showLobby(lobbyMessage.getNicknameList(), lobbyMessage.getMaxPlayers()));
            default: // Should never reach this condition
                break;
        }
    }


    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param portStr the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
