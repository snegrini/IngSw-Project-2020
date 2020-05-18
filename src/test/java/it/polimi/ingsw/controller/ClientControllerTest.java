package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.view.cli.Cli;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientControllerTest {

    ClientController clientController;

    @Before
    public void setUp() {
        clientController = new ClientController(new Cli()); // ClientController needs a View
    }


    @Test
    public void isValidIpAddress() {
        assertTrue(clientController.isValidIpAddress("192.168.1.5"));
        assertFalse(clientController.isValidIpAddress("268.654.321.123"));
    }

    @Test
    public void isValidPort() {
        assertTrue(clientController.isValidPort(3333));
        assertTrue(clientController.isValidPort(16847));

        assertFalse(clientController.isValidPort(-1));
        assertFalse(clientController.isValidPort(65536));
    }
}
