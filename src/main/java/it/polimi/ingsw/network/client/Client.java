package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.observer.Observable;

/**
 * Abstract class to communicate with the server. Every type of connection must implement this interface.
 */
public abstract class Client extends Observable {

    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * Asynchronously reads a message from the server and notify the ClientController.
     */
    public abstract void readMessage();

    /**
     * Disconnects from the server.
     */
    public abstract void disconnect();
}
