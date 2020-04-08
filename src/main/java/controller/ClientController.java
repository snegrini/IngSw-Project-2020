package controller;

import network.client.Client;
import network.client.SocketClient;
import view.ViewListener;
import view.cli.Cli;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientController implements ViewListener {

    private Cli cli;
    private Client client;

    // TODO generalize view type received as parameter. Could also be GUI?
    public ClientController(Cli cli) {
        this.cli = cli;
    }

    @Override
    public void doConnect(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
        } catch (IOException e) {
            // TODO show error in view and return the old view.
            System.out.println("could not contact server");
        }
        cli.askNickname();
    }

    @Override
    public void checkNickname(String nickname) {
        while (!"".equals(nickname)) {
            Future<String> stringFuture = client.requestConversion(nickname);
            String response = null;

            int seconds = 0;
            while (response == null) {
                System.out.println("been waiting for " + seconds + " seconds");
                try {
                    response = stringFuture.get(1, TimeUnit.SECONDS);
                } catch (InterruptedException | TimeoutException e) {
                } catch (ExecutionException e) {
                    System.out.println("server not reachable");
                    return;
                }
                seconds++;
            }
            System.out.println(response);
            cli.askNickname();
        }

        client.disconnect();
    }


}
