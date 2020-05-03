package it.polimi.ingsw.PSP016.network.client;

import it.polimi.ingsw.PSP016.network.message.Message;
import it.polimi.ingsw.PSP016.observer.Observable;

import java.util.logging.Logger;

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

    /**
     * Enable a heartbeat (ping messages) to keep the connection alive.
     *
     * @param enabled Set this argument to {@code true} to enable the heartbeat.
     *                Set to {@code false} to kill the heartbeat.
     */
    public abstract void enablePinger(boolean enabled);
}
