package network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SocketClientHandler implements ClientHandler, Runnable {
    private Socket client;
    private int idClient;

    SocketClientHandler(Socket client, int idClient) {
        this.client = client;
        this.idClient = idClient;
    }

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("client " +  client.getInetAddress() + " connection dropped");
        }
    }


    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

        try {
            while (true) {
                Object next = input.readObject();
                String str = (String) next;

                try {
                    /* simulate a complex computation */
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                }

                output.writeObject(str.toUpperCase());
            }

            /*output.writeObject("You're the 1st player");

            if (idClient == 1) {
                output.writeObject("You're the 1st player. How many players do you want in your game?");
                int numPlayers = input.readInt();
            } else {

                /*while(true) {
                    // read command and execute it
                }

            }*/

        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Invalid stream from client");
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
}
