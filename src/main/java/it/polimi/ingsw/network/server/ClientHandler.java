package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;

/**
 * EInterface to handle clients. very type of connection must implement this interface.
 */
public interface ClientHandler {

    /**
     * @return the connection status
     */
    boolean isConnected();

    /**
     * Disconnects from the client.
     */
    void disconnect();

    /**
     * Sends a message to the client.
     *
     * @param message the message to be sent.
     */
    void sendMessage(Message message);
}
