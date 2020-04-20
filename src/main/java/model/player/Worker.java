package model.player;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.board.Space;
import model.effects.Effect;
import model.enumerations.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Worker {

    private final Color color;
    private Position position;
    private MoveHistory moveHistory;
    //private List<Effect> effects;

    public Worker(Color color, Position position) {
        this.color = color;
        this.position = position;
        this.moveHistory = new MoveHistory(position, 0);
        //this.effects = new ArrayList<>();
    }

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
     * @return a List of adjacent positions to the worker's position
     */
    public List<Position> getPossibleBuilds() {

        List<Position> positionList = new ArrayList<Position>();

        //Check all adjacent positions clockwise
        if (position.getColumn() - 1 >= 0 && position.getRow() - 1 >= 0)
            positionList.add(new Position(position.getRow() - 1, position.getColumn() - 1));
        if (position.getRow() - 1 >= 0)
            positionList.add(new Position(position.getRow() - 1, position.getColumn()));
        if (position.getColumn() + 1 < 5 && position.getRow() - 1 >= 0)
            positionList.add(new Position(position.getRow() - 1, position.getColumn() + 1));
        if (position.getColumn() + 1 < 5)
            positionList.add(new Position(position.getRow(), position.getColumn() + 1));
        if (position.getColumn() + 1 < 5 && position.getRow() + 1 < 5)
            positionList.add(new Position(position.getRow() + 1, position.getColumn() + 1));
        if (position.getRow() + 1 <= 5)
            positionList.add(new Position(position.getRow() + 1, position.getColumn()));
        if (position.getColumn() - 1 >= 0 && position.getRow() + 1 < 5)
            positionList.add(new Position(position.getRow() + 1, position.getColumn() - 1));
        if (position.getColumn() - 1 >= 0)
            positionList.add(new Position(position.getRow(), position.getColumn() - 1));

        return positionList;
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

    public MoveHistory getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(MoveHistory moveHistory) {
        this.moveHistory = moveHistory;
    }

    /*public List<Effect> getEffects() {
        return List.copyOf(effects);
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }*/


}