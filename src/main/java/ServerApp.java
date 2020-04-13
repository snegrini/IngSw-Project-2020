import network.server.Server;
import network.server.SocketServer;

public class ServerApp {

    public static void main(String[] args) {
        // TODO parse cmd parameteres and pass server port
        int serverPort = 16847;

        Server server = new Server();

        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
        Server.LOGGER.info("Socket server started");

    }


}
