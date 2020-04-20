package network.server;

import controller.GameController;
import network.message.Message;
import view.View;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Server {

    private GameController gameController;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Adds a client to be managed by the server instance.
     *
     * @param nickname the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        gameController.addVirtualView(nickname, new VirtualView(clientHandler));
    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname the VirtualView to be removed.
     */
    public void removeClient(String nickname) {
        gameController.removeVirtualView(nickname);
    }

    public void onMessageReceived(Message message) {
        gameController.onMessageReceived(message);
    }
}
