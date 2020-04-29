import controller.GameController;
import network.server.Server;
import network.server.SocketServer;

public class ServerApp {

    public static void main(String[] args) {
        int serverPort = 16847; // default value

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--port") || args[i].equals("-p")) {
                serverPort = Integer.parseInt(args[i + 1]);
            }
        }

        GameController gameController = new GameController();
        Server server = new Server(gameController);

        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
        Server.LOGGER.info("Socket server started on port " + serverPort + ".");
    }

}
