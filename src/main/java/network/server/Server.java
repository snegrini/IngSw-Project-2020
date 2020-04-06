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
        socketPort = 16847;  // FIXME
        SocketServer serverSocket = new SocketServer(this, socketPort);
        Thread thread = new Thread(serverSocket, "socketserver_");
        thread.start();
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
