package it.polimi.ingsw.PSP016;

import it.polimi.ingsw.PSP016.controller.ClientController;
import it.polimi.ingsw.PSP016.view.View;
import it.polimi.ingsw.PSP016.view.cli.Cli;
import it.polimi.ingsw.PSP016.view.gui.Gui;

public class ClientApp {
    public static void main(String[] args) {

        boolean cliParam = false; // default value

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--cli") || args[i].equals("-c")) {
                cliParam = true;
            }
        }

        View view = null;

        if (cliParam) {
            view = new Cli();
        } else {
            view = new Gui();
        }

        ClientController clientcontroller = new ClientController(view);
        view.addObserver(clientcontroller);
        view.init();
    }
}
