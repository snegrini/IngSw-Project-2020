package it.polimi.ingsw.PSP016.network.server;

import it.polimi.ingsw.PSP016.network.message.Message;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
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
