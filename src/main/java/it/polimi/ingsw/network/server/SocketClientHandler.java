package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

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
            Server.LOGGER.severe("Client " + client.getInetAddress() + " connection dropped.");
            disconnect();
        }
    }

    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("Client connected from " + client.getInetAddress());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message message = (Message) input.readObject();

                if (message != null && message.getMessageType() != MessageType.PING) {
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
