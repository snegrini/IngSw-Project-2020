package it.polimi.ingsw.model.board;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class uses a Two-Dimensional Coordinate System to identify positions inside the {@link Board}.
 */
public class Position implements Serializable {
    private static final long serialVersionUID = 6198190192800214046L;
    private int row;
    private int column;

    /**
     * Default constructor.
     * A position must always have an initial row and an initial column.
     *
     * @param row    the initial row to be set.
     * @param column the initial column to be set.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Copy constructor.
     *
     * @param position the {@code Position} object to be copied.
     */
    public Position(Position position) {
        this.row = position.row;
        this.column = position.column;
    }

    /**
     * Returns the row of the position.
     *
     * @return the row of the position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of the position.
     *
     * @param row the row to be set.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Returns the column of the position.
     *
     * @return the column of the position.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column of the position.
     *
     * @param column the column to be set.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
