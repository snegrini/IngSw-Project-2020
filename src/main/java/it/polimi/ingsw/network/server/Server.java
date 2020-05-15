package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Server {

    private GameController gameController;

    private Map<String, ClientHandler> clientHandlerMap;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerMap = new HashMap<>();
    }

    /**
     * Adds a client to be managed by the server instance.
     *
     * @param nickname the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        VirtualView vv = new VirtualView(clientHandler);
        if (gameController.checkLoginNickname(nickname, vv)) {
            clientHandlerMap.put(nickname, clientHandler);
            gameController.loginHandler(nickname, vv);
        }
    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname the VirtualView to be removed.
     */
    public void removeClient(String nickname) {
        clientHandlerMap.remove(nickname);
        gameController.removeVirtualView(nickname);
    }

    public void onMessageReceived(Message message) {
        gameController.onMessageReceived(message);
    }

    /**
     * Handles the disconnection of a client.
     *
     * @param clientHandler the client disconnecting.
     */
    public void onDisconnect(ClientHandler clientHandler) {
        String nickname = getNicknameFromClientHandler(clientHandler);

        if (nickname != null) {
            removeClient(nickname);
            gameController.broadcastGenericMessage(nickname + " disconnected from the server. GAME ENDED.");
        }
    }

    /**
     * Returns the corresponding nickname of a ClientHandler.
     *
     * @param clientHandler the client handler.
     * @return the corresponding nickname of a ClientHandler.
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
