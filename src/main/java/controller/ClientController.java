package controller;

import model.God;
import model.enumerations.Color;
import network.client.Client;
import network.client.SocketClient;
import network.message.LoginRequest;
import network.message.Message;
import view.View;
import observer.ViewObserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientController implements ViewObserver {

    private View view;
    private Client client;

    public ClientController(View view) {
        this.view = view;
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
        } catch (IOException e) {
            // TODO show error in view and return the old view.
            System.out.println("could not contact server");
        }
        view.askNickname();
    }

    @Override
    public void onUpdateNickname(String nickname) {

        Message message = new LoginRequest(nickname);

        while (!"".equals(nickname)) {
            Future<Message> stringFuture = client.sendMessage(message);
            Message response = null;

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
            view.askNickname();
        }

        client.disconnect();
    }

    @Override
    public void onUpdatePlayersNumber(int playerNumber) {
        // TODO check user input.

        //view.askPlayerNumber();

    }

    @Override
    public void onUpdateWorkersColor(Color color) {

    }

    @Override
    public void onUpdateGod(God god) {

    }

    @Override
    public void onUpdateWorkerToMove(int chosenRow, int chosenColumn) {

    }

    @Override
    public void onUpdateWorkerPosition(int chosenRow, int chosenColumn) {

    }

    @Override
    public void onUpdateBuild(int chosenRow, int chosenColumn) {

    }

}
