package model.player;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.effects.Effect;
import model.enumerations.Color;

import java.util.ArrayList;
import java.util.List;

public class Worker {

    private static Color color;
    private Position position;
    private MoveHistory moveHistory;
    private List<Effect> effects;

    public Worker(Position position, List<Effect> effects) {
        this.position = position;
        this.moveHistory = new MoveHistory(position, 0);
        this.effects = effects;
    }

    public void build(Position p) {
        // Calls controller.
    }

    /**
     * Move the Worker to the given position.
     *
     * @param position a valid position to move
     */
    public void move(Position position) {
        Board board = Game.getInstance().getBoard();

        // Check if destination space is free:
        if (board.getSpace(position).isFree()) {

            // Check level compatibility
            int pLevel = board.getSpace(position).getLevel();
            int actualLevel = board.getSpace(position).getLevel();

            if (pLevel - actualLevel <= 1 || pLevel - actualLevel >= -1) {

                // Executing move
                updateMoveHistory(this.position, board.getSpace(this.position).getLevel());
                this.position = position; // Worker is now in the new position
            }
        }
    }

    /**
     *
     * @return a List of adjacent positions to the worker's position
     */
    public List<Position> getRange() {

        List<Position> positionList = new ArrayList<Position>();


        if (position.getColumn()-1>=0 && position.getRow()-1>=0)
            positionList.add(new Position(position.getRow() - 1, position.getColumn() - 1));
        if (position.getRow()-1>=0)
            positionList.add(new Position(position.getRow()-1, position.getColumn()));
        if (position.getColumn()+1<=5 && position.getRow()-1>=0)
            positionList.add(new Position(position.getRow()-1, position.getColumn()+1));
        if (position.getColumn()+1<=5)
            positionList.add(new Position(position.getRow(), position.getColumn()+1));
        if (position.getColumn()+1<=5 && position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()+1));
        if (position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()));
        if (position.getColumn()-1>=0 && position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()-1));
        if (position.getColumn()-1>=0)
            positionList.add(new Position(position.getRow(), position.getColumn()-1));

        return positionList;
    }


    /**
     * Update the worker move history.
     *
     * @param position worker's position in the previous turn
     * @param level worker's level in the previous turn
     */
    private void updateMoveHistory(Position position, int level){
        moveHistory.setLastPosition(position);
        moveHistory.setLastLevel(level);
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor(Color color) {
        Worker.color = color;
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

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
}