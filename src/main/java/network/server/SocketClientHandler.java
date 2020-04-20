package network.server;

import network.message.Message;
import network.message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class SocketClientHandler implements ClientHandler, Runnable {
    private Socket client;
    private SocketServer socketServer;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public SocketClientHandler(SocketServer socketServer, Socket client) {
        this.socketServer = socketServer;
        this.client = client;

        try {
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("client " + client.getInetAddress() + " connection dropped");
        }
    }

    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("Connected to " + client.getInetAddress());

        try {
            while (true) {
                Message message = (Message) input.readObject();

                if (message != null) {
                    if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
                        socketServer.addClient(message.getNickname(), this);
                    }
                    socketServer.onMessageReceived(message);
                }
            }
        } catch (ClassCastException | ClassNotFoundException e) {
            Server.LOGGER.severe("Invalid stream from client");
        }

        client.close();
    }

    @Override
    public boolean isConnected() {
        // TODO
        return true;
    }

    @Override
    public void disconnect() {
        // TODO
    }

    @Override
    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
            disconnect();
        }
    }
}
