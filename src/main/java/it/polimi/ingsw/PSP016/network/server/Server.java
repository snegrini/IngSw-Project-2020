package it.polimi.ingsw.PSP016.network.server;

import it.polimi.ingsw.PSP016.controller.GameController;
import it.polimi.ingsw.PSP016.network.message.Message;
import it.polimi.ingsw.PSP016.view.VirtualView;

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
