package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;

public class ClientApp {
    public static void main(String[] args) {

        boolean cliParam = true; // default value

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--cli") || args[i].equals("-c")) {
                cliParam = Boolean.parseBoolean(args[i + 1]);
            }
        }

        View view = null;

        if (cliParam) {
            view = new Cli();
        } else {
            //View view = new Gui();
        }

        ClientController clientcontroller = new ClientController(view);
        view.addObserver(clientcontroller);
        view.init();
    }
}
