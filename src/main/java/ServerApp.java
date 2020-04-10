import controller.ServerController;
import model.Game;
import network.server.Server;

public class ServerApp {

    public static void main(String[] args) {
        // TODO parse cmd parameteres and pass server port
        int serverPort = 16847;
        Server server = new Server(serverPort);


        ServerController serverController = new ServerController(server, Game.getInstance());

    }


}
