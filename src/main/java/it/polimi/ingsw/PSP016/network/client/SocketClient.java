package it.polimi.ingsw.PSP016.network.client;

import it.polimi.ingsw.PSP016.network.message.Message;
import it.polimi.ingsw.PSP016.network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient extends Client {

    private Socket socket;

    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private ExecutorService readExecutionQueue = Executors.newSingleThreadExecutor();
    private ExecutorService executionQueue = Executors.newSingleThreadExecutor();


    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.outputStm = new ObjectOutputStream(socket.getOutputStream());
        this.inputStm = new ObjectInputStream(socket.getInputStream());
    }

    public void readMessage() {
        readExecutionQueue.execute(() -> {
            while (true) { // FIXME
                Message message = null;
                try {
                    message = (Message) inputStm.readObject();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                notifyObserver(message);

            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        executionQueue.execute(() -> {
            try {
                outputStm.writeObject(message);
                // FIXME
            } catch (IOException e) {
                Server.LOGGER.severe(e.getMessage());
            }
        });
    }

    @Override
    public void disconnect() {
        executionQueue.execute(() -> {
            try {
                socket.close();
            } catch (IOException e) {
            }
        });
        executionQueue.shutdown();
    }
}
