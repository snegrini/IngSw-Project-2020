package network.server;

import network.message.Message;

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

        while(true) {
            try {
                Socket client = serverSocket.accept();

                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                Server.LOGGER.severe("Connection dropped");
            }
        }
    }

    public void addClient(String nickname, ClientHandler clientHandler) {
        server.addClient(nickname, clientHandler);
    }

    public void removeClient(String nickname) {
        server.removeClient(nickname);
    }

    public void onMessageReceived(Message message) {

        server.onMessageReceived(message);
    }

}
