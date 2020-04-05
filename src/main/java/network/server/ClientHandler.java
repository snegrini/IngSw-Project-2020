package network.server;

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
}
