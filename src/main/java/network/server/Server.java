package network.server;

import java.io.IOException;


public class Server implements Runnable{

    private int socketPort;


    private Server() {
        startServers();
    }

    private void startServers() {
        SocketServer serverSocket = new SocketServer(this, socketPort);
        serverSocket.startServer();
        System.out.println("Socket server started");

        // free to implement RMI server too
    }


    public void main() throws IOException {

        new Server();

    }

    public void run() {

    }


}
