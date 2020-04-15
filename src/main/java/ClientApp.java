import controller.ClientController;
import view.View;
import view.cli.Cli;

public class ClientApp {
    public static void main(String[] args) {
        // TODO parse cmd parameters to select CLI or GUI view.

        View view = new Cli();
        ClientController clientcontroller = new ClientController(view);
        view.addObserver(clientcontroller);
        view.init();
    }
}
