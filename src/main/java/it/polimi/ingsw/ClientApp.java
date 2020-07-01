package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.JavaFXGui;
import javafx.application.Application;

import java.util.logging.Level;

/**
 * Main of the client app.
 */
public class ClientApp {

    public static void main(String[] args) {

        boolean cliParam = false; // default value

        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                cliParam = true;
                break;
            }
        }

        if (cliParam) {
            Client.LOGGER.setLevel(Level.WARNING);
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        } else {
            Application.launch(JavaFXGui.class);
        }
    }
}
