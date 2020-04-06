package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {

    private final Server server;
    private final int port;
    private ServerSocket serverSocket;


    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Server didn't start");
        }

        int idClient = 1;

        while(true) {
            try {
                Socket client = serverSocket.accept();
                idClient += 1;

                SocketClientHandler clientHandler = new SocketClientHandler(client, idClient);
                Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
                thread.start();

            } catch (IOException e) {
                Server.LOGGER.severe("Connection dropped");
            }
        }
    }



}
