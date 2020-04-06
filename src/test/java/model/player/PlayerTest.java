package model.player;

import model.board.Position;
import model.enumerations.Color;
import model.enumerations.PlayerState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player("sam");
        player.setState(PlayerState.START);
    }

    @After
    public void tearDown() throws Exception {
        player = null;
    }

    @Test
    public void getNickname() {
        assertEquals("sam", player.getNickname());
    }

    @Test
    public void getWorkerByPosition() {
        Worker worker = new Worker(Color.BLUE, new Position(0, 0));
        player.addWorker(worker);

        assertEquals(worker, player.getWorkerByPosition(new Position(0,0)));
    }

    @Test
    public void getGod() {

    }

    @Test
    public void setGod() {
    }

    @Test
    public void getState() {
        assertEquals(PlayerState.START, player.getState());
    }

    @Test
    public void setState() {
        player.setState(PlayerState.BUILD);
        assertEquals(PlayerState.BUILD,player.getState());
    }

    @Test
    public void testEquals() {
        Player otherPlayer = new Player("sam");
        assertTrue(player.equals(otherPlayer));
    }

    @Test
    public void testHashCode() {
        int hash = Objects.hash("sam");
        assertEquals(hash, player.hashCode());
    }
}