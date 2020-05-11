package it.polimi.ingsw.model.board;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class PositionTest {

    private Position position;

    @Before
    public void setUp() throws Exception {
        position = new Position(3,4);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRow() {
        assertEquals(3, position.getRow());
    }

    @Test
    public void setRow() {
        position.setRow(2);
        assertEquals(2, position.getRow());
    }

    @Test
    public void getColumn() {
        assertEquals(4, position.getColumn());
    }

    @Test
    public void setColumn() {
        position.setColumn(2);
        assertEquals(2, position.getColumn());
    }

    @Test
    public void equals_DifferentPosition_False() {
        Position testPosition = new Position(2, 1);
        assertFalse(position.equals(testPosition));
    }

    @Test
    public void testCopyConstructor() {
        Position testPosition = new Position(position);
        assertEquals(position, testPosition);
        assertNotSame(position, testPosition);
    }

    @Test
    public void equals_SamePosition_True() {
        Position testPosition = new Position(3, 4);
        assertTrue(position.equals(testPosition));
    }

    @Test
    public void testHashCode() {
        int expectedHash = Objects.hash(3, 4);
        assertEquals(expectedHash, position.hashCode());
    }
}