import controller.ClientController;
import view.View;
import view.cli.Cli;

public class ClientApp {
    public static void main(String[] args) {
        // TODO parse cmd parameters to select CLI or GUI view.

        // FIXME pass view to controller in order to manage from there which
        //       view to show. Change also the flow-control in the CLI view.


        View cli = new Cli();
        ClientController clientcontroller = new ClientController(cli);
        cli.addObserver(clientcontroller);
        cli.init();
    }
}
