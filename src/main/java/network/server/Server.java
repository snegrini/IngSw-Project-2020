package network.server;

import java.util.logging.Logger;


public class Server { /*implements Runnable {*/

    private int socketPort;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public Server() {
        startServers();
    }

    /**
     * Start a server for each type of network tech used.
     * In this case, only Socket is implemented.
     */
    private void startServers() {
        socketPort = 16847;  // FIXME
        SocketServer serverSocket = new SocketServer(this, socketPort);
        Thread thread = new Thread(serverSocket, "socketserver_");
        thread.start();
        LOGGER.info("Socket server started");
    }

    /*@Override
    public void run() {

    }*/
}
