package controller;


import model.Game;
import network.server.Server;

public class ServerController {

    private final Server server;
    private final Game gameInstance;


    public ServerController(Server server, Game gameInstance) {
        this.server = server;
        this.gameInstance = gameInstance;
    }

    public Server getServer() {
        return server;
    }

    public Game getGameInstance() {
        return gameInstance;
    }


}
