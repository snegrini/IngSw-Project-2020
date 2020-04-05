package network.server;

import java.util.logging.Logger;


public class Server { /*implements Runnable {*/

    private int socketPort;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private Server() {
        startServers();
    }

    /**
     * Start a server for each type of network tech used.
     * In this case, only Socket is implemented.
     */
    private void startServers() {
        SocketServer serverSocket = new SocketServer(this, 16847);
        // FIXME SocketServer serverSocket = new SocketServer(this, socketPort);
        serverSocket.startServer();
        LOGGER.info("Socket server started");
    }


    public static void main(String[] args) {
        // TODO parse cmd parameteres and pass server port
        new Server();
    }


    /*@Override
    public void run() {

    }*/
}
