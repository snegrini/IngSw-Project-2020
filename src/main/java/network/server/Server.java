package network.server;

import network.message.Message;
import view.View;
import view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Server {

    private final Map<String, VirtualView> clients;

    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server() {
        this.clients = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Adds a client to be managed by the server instance.
     * Each VirtualView corresponds to a single client view.
     *
     * @param nickname the nickname associated with the client.
     * @param virtualView the VirtualView associated with the client.
     */
    public void addClient(String nickname, VirtualView virtualView) {
        clients.put(nickname, virtualView);
    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname the VirtualView to be removed.
     */
    public void removeClient(String nickname) {
        clients.remove(nickname);
    }

    /**
     * Sends a message to the client by using the corresponding
     * client handler into the virtualview.
     *
     * @param message the message to be sent.
     */
    public void sendMessage(String nickname, Message message) {
        VirtualView virtualview = clients.get(nickname);
        virtualview.getClientHandler().sendMessage(message);
    }

}
