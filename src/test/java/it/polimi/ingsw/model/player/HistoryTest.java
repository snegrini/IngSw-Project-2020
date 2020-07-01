package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Position;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the {@link History} methods.
 */
public class HistoryTest {

    private History history;

    @Before
    public void setUp() {
        history = new History();
    }

    @After
    public void tearDown() {
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

}