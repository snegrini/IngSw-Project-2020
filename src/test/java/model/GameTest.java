package model;

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
        this.instance = null;
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
}