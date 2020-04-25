package model.player;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.board.Space;
import model.enumerations.Color;

import java.util.List;
import java.util.stream.Collectors;

public class Worker {

    private final Color color;
    private Position position;
    private MoveHistory moveHistory;

    public Worker(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.moveHistory = new MoveHistory(position, 0);
    }

    /**
     * Builds a single block over the {@code Space} at the given position.
     *
     * @param position the position of the {@code Space} where to build.
     */
    public void build(Position position) {
        Game.getInstance().getBoard().getSpace(position).increaseLevel(1);
    }

    /**
     * Move the Worker to the given position.
     *
     * @param position a valid position to move
     */
    public void move(Position position) {
        updateMoveHistory(this.position, Game.getInstance().getBoard().getSpace(this.position).getLevel());
        this.position = position; // Worker is now in the new position
    }

    /**
     * Returns the adjacent positions where this worker can normally build.
     * Non-free spaces will be ignored @see model.Space#isFree.
     *
     * @return a list of positions where this worker can build.
     */
    public List<Position> getPossibleBuilds() {
        Board board = Game.getInstance().getBoard();

        return board.getNeighbours(position).stream()
                .filter(pos -> board.getSpace(pos).isFree())
                .collect(Collectors.toList());
    }

    /**
     * Returns a {@code List} of adjacent and "level compatible" positions to the worker's position.
     *
     * @return a {@code List} of adjacent and "level compatible" positions to the worker's position.
     */
    public List<Position> getPossibleMoves() {
        Board board = Game.getInstance().getBoard();
        Space currentSpace = board.getSpace(position);

        return board.getNeighbours(position).stream()
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) <= currentSpace.getLevel())
                .filter(pos -> currentSpace.compareTo(board.getSpace(pos)) >= -1)
                .collect(Collectors.toList());
    }

    /**
     * Update the worker move history.
     *
     * @param position worker's position in the previous turn
     * @param level    worker's level in the previous turn
     */
    private void updateMoveHistory(Position position, int level) {
        moveHistory.setLastPosition(position);
        moveHistory.setLastLevel(level);
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Returns a copy of {@code MoveHistory} of this worker.
     *
     * @return a copy of {@code MoveHistory} of this worker.
     */
    public MoveHistory getMoveHistory() {
        return new MoveHistory(this.moveHistory);
    }
}
