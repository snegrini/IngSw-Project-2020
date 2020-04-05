package network.client;

import java.util.concurrent.Future;

/**
 *  Interface to communicate with the server. Every type of connection must implement this interface.
 */
public interface Client {

    Future<String> requestConversion(String input);

    void disconnect();
}
