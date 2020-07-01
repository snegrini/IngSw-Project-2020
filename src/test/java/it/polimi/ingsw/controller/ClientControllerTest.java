package it.polimi.ingsw.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the static methods of the {@link ClientController} which checks the server info.
 */
public class ClientControllerTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void isValidIpAddress() {
        assertTrue(ClientController.isValidIpAddress("192.168.1.5"));
        assertFalse(ClientController.isValidIpAddress("268.654.321.123"));
    }

    @Test
    public void isValidPort() {
        assertTrue(ClientController.isValidPort("3333"));
        assertTrue(ClientController.isValidPort("16847"));

        assertFalse(ClientController.isValidPort("-1"));
        assertFalse(ClientController.isValidPort("65536"));
        assertFalse(ClientController.isValidPort("string"));
    }
}
