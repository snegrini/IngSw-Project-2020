package model;

import model.enumerations.GameState;
import model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {

    private Game instance;

    @Before
    public void setUp() throws Exception {
        this.instance = Game.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        Game.resetInstance();
    }

    @Test
    public void getInstance() {
        assertEquals(instance, Game.getInstance());
    }

    @Test
    public void getPlayerByNickname_NicknameFound() {
        Player player = new Player("anna");
        instance.addPlayer(player);
        assertEquals(player, instance.getPlayerByNickname("anna"));
    }

    @Test
    public void getPlayerByNickname_NicknameNotFound() {
        assertNull(instance.getPlayerByNickname("mario"));
    }

    @Test
    public void addPlayer() {
        instance.addPlayer(new Player("sam"));
        instance.addPlayer(new Player("andre"));

        assertEquals(2, instance.getNumCurrentPlayers());
    }

    @Test
    public void setChosenMaxPlayers_inRange() {
        assertTrue(instance.setChosenMaxPlayers(3));
        assertEquals(3, instance.getChosenPlayersNumber());
    }

    @Test
    public void setChosenMaxPlayers_outOfBound() {
        assertFalse(instance.setChosenMaxPlayers(5));
    }

    @Test
    public void isNicknameTaken_True() {
        instance.addPlayer(new Player("sam"));
        assertTrue(instance.isNicknameTaken("sam"));
    }

    @Test
    public void isNicknameTaken_False() {
        assertFalse(instance.isNicknameTaken("anto"));
    }

    @Test
    public void testGameState() {
        GameState gameState = GameState.LOGIN;
        assertEquals(gameState, GameState.LOGIN);
    }
}