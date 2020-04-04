package network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread{

    private final Server server;
    private final int port;
    private ServerSocket serverSocket;


    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            start();
        } catch (IOException e) {
            System.out.println("Server didn't start");
        }
    }

    public void run() {

        int idClient = 0;

        while(true) {
            try {
                Socket client = serverSocket.accept();
                idClient += 1;

                ClientHandler clientHandler = new ClientHandler(client, idClient);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();

            } catch (IOException e) {
                System.out.println("Connection dropped");
            }
        }
    }



}
