package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.effects.SimpleEffect;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.PhaseType;
import it.polimi.ingsw.model.enumerations.PlayerState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

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
        Worker worker = new Worker(new Position(0, 0));
        player.addWorker(worker);

        assertEquals(worker, player.getWorkerByPosition(new Position(0, 0)));
    }

    @Test
    public void godGetterAndSetter() {
        God god = new God.Builder("Name Test")
                .withCaption("Caption Test")
                .withDescription("Description Test")
                .withEffects(List.of(new SimpleEffect(PhaseType.YOUR_BUILD)))
                .build();

        player.setGod(god);
        assertEquals(god, player.getGod());
    }

    @Test
    public void getState() {
        assertEquals(PlayerState.START, player.getState());
    }

    @Test
    public void setState() {
        player.setState(PlayerState.BUILD);
        assertEquals(PlayerState.BUILD, player.getState());
    }

    @Test
    public void initWorkers_getWorkersPositions() {
        player.addWorker(new Worker(Color.BLUE));
        player.addWorker(new Worker(Color.BLUE));

        List<Position> positionList = List.of(new Position(0, 0), new Position(2, 4));
        player.initWorkers(positionList);
        assertEquals(positionList, player.getWorkersPositions());
    }

    @Test
    public void testEquals() {
        Player otherPlayer = new Player("sam");
        assertEquals(player, otherPlayer);
    }

    @Test
    public void testHashCode() {
        int hash = Objects.hash("sam");
        assertEquals(hash, player.hashCode());
    }
}