package network.server;

import model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket client;
    private int idClient;

    ClientHandler(Socket client, int idClient) {
        this.client = client;
        this.idClient = idClient;
    }

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " +  client.getInetAddress() + " connection dropped");
        }
    }


    private  void handleClientConnection() throws IOException {
        System.out.println("Connected to " + client.getInetAddress());

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

        try {
            // INIT:
            if(idClient == 1) {
                output.writeObject("You're the 1st player. How many players do You want in your game?");
                int numPlayers = input.readInt();
            } else {

                if (idClient < )
                while(true) {
                    /* read command and execute it */
                }

            }




        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("Invalid stream from client");
        }

        client.close();
    }
}
