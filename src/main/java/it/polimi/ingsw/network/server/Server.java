package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Main server class that starts a socket server.
 * It can handle different types of connections.
 */
public class Server {

    private final GameController gameController;

    private final Map<String, ClientHandler> clientHandlerMap;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Adds a client to be managed by the server instance.
     *
     * @param nickname      the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        VirtualView vv = new VirtualView(clientHandler);

        if (!gameController.isGameStarted()) {
            if (gameController.checkLoginNickname(nickname, vv)) {
                clientHandlerMap.put(nickname, clientHandler);
                gameController.loginHandler(nickname, vv);
            }
        } else {
            vv.showLoginResult(true, false, null);
            clientHandler.disconnect();
        }

    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname      the VirtualView to be removed.
     * @param notifyEnabled set to {@code true} to enable a lobby disconnection message, {@code false} otherwise.
     */
    public void removeClient(String nickname, boolean notifyEnabled) {
        clientHandlerMap.remove(nickname);
        gameController.removeVirtualView(nickname, notifyEnabled);
        LOGGER.info(() -> "Removed " + nickname + " from the client list.");
    }

    /**
     * Forwards a received message from the client to the GameController.
     *
     * @param message the message to be forwarded.
     */
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

            boolean gameStarted = gameController.isGameStarted();
            removeClient(nickname, !gameStarted); // enable lobby notifications only if the game didn't start yet.

            // Resets server status only if the game was already started.
            // Otherwise the server will wait for a new player to connect.
            if (gameStarted) {
                gameController.broadcastDisconnectionMessage(nickname, " disconnected from the server. GAME ENDED.");

                gameController.endGame();
                clientHandlerMap.clear();
            } else if(gameController.isGameFinished()) {
                gameController.endGame();
                clientHandlerMap.clear();
            }
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
