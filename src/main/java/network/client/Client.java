package network.client;

import network.message.Message;

import java.util.concurrent.Future;

/**
 * Interface to communicate with the server. Every type of connection must implement this interface.
 */
public interface Client {

    /**
     * Sends a message to the server and waits for the response.
     *
     * @param message the message to be sent.
     * @return the response Message received from the server.
     */
    Future<Message> sendMessage(Message message);

    void disconnect();
}
