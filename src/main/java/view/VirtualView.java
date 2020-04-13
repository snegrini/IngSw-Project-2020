package view;

import network.server.ClientHandler;
import observer.ModelObserver;

public class VirtualView extends View implements ModelObserver {
    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void init() {

    }

    @Override
    public void askServerInfo() {

    }

    @Override
    public void askNickname() {

    }

    @Override
    public void askPlayerNumber() {

    }

    @Override
    public void update() {

    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }
}
