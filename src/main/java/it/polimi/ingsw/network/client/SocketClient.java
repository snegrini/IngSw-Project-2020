package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.message.ErrorMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.PingMessage;
import it.polimi.ingsw.view.gui.SceneController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketClient extends Client {

    private Socket socket;

    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private ExecutorService readExecutionQueue;
    private ExecutorService sendExecutionQueue;
    private ScheduledExecutorService pinger;


    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.outputStm = new ObjectOutputStream(socket.getOutputStream());
        this.inputStm = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
        this.sendExecutionQueue = Executors.newSingleThreadExecutor();
        this.pinger = Executors.newSingleThreadScheduledExecutor();
    }

    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message = null;
                try {
                    message = (Message) inputStm.readObject();
                    SceneController.LOGGER.info("RECEIVED: " + message.getMessageType());
                } catch (IOException | ClassNotFoundException e) {
                    message = new ErrorMessage(null, "connection lost with the server.");
                    readExecutionQueue.shutdownNow();
                }
                notifyObserver(message);
            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        /*sendExecutionQueue.execute(() -> {
            try {
                outputStm.writeObject(message);
            } catch (IOException e) {
                notifyObserver(new ErrorMessage(null, "could not send message."));
                disconnect();
            }
        });*/
        try {
            outputStm.writeObject(message);
        } catch (IOException e) {
            notifyObserver(new ErrorMessage(null, "could not send message."));
            disconnect();
        }
    }

    public void enablePinger(boolean enabled) {
        if (enabled) {
            pinger.scheduleAtFixedRate(() -> {
                sendMessage(new PingMessage());
            }, 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }

    /**
     * Disconnect the socket from the server.
     */
    @Override
    public void disconnect() {
        sendExecutionQueue.execute(() -> {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                notifyObserver(new ErrorMessage(null, "could not disconnect."));
            }
        });
        sendExecutionQueue.shutdown();
    }
}
