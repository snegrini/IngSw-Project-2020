package network.client;

import network.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main( String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        /* open connection */
        Socket server;

        try {
            server = new Socket(ip, Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Server unreachable");
            return;
        }
        System.out.println("Connected");

        try {
            ObjectOutputStream output = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(server.getInputStream());

            /* command here */

        } catch (IOException e) {
            System.out.println("Server has died");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Protocol violation");
        }

    }
}
