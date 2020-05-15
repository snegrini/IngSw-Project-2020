package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.JavaFXGui;
import javafx.application.Application;

public class ClientApp {
    public static void main(String[] args) {

        boolean cliParam = false; // default value

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--cli") || args[i].equals("-c")) {
                cliParam = false;
            }
        }

        if (cliParam) {
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        } else {
            Application.launch(JavaFXGui.class);
        }


    }
}
