package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Position;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HistoryTest {

    private History history;

    @Before
    public void setUp() throws Exception {
        history = new History();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setAndGetMovePosition() {
        Position lastPosition = new Position(1, 3);
        history.setMovePosition(lastPosition);
        assertEquals(lastPosition, history.getMovePosition());
    }

    @Test
    public void setAndGetMoveLevel() {
        history.setMoveLevel(2);
        assertEquals(2, history.getMoveLevel());
    }

    @Test
    public void setAndGetBuildPosition() {
        Position lastPosition = new Position(1, 3);
        history.setBuildPosition(lastPosition);
        assertEquals(lastPosition, history.getBuildPosition());
    }

    @Test
    public void setAndGetBuildLevel() {
        history.setBuildLevel(3);
        assertEquals(3, history.getBuildLevel());
    }


}