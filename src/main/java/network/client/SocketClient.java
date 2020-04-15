package network.client;

import network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SocketClient implements Client {

    private Socket socket;

    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private ExecutorService executionQueue = Executors.newSingleThreadExecutor();


    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.outputStm = new ObjectOutputStream(socket.getOutputStream());
        this.inputStm = new ObjectInputStream(socket.getInputStream());
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

    @Override
    public Future<Message> sendMessage(Message message) {
        return executionQueue.submit(() -> {
            outputStm.writeObject(message);
            return (Message) inputStm.readObject();
        });
    }

}
