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
        // Executing move
        updateMoveHistory(this.position, Game.getInstance().getBoard().getSpace(this.position).getLevel());
        this.position = position; // Worker is now in the new position
    }

    /**
     *
     * @param position1 obj of class Position
     * @param position2 obf of class Position
     * @return true if difference of position's space's levels is not greater than 1
     */
    public boolean checkLevel(Position position1, Position position2){

        int level1 =  Game.getInstance().getBoard().getSpace(position1).getLevel();
        int level2 = Game.getInstance().getBoard().getSpace(position2).getLevel();

        return (level1 - level2) <= 1 || (level1 - level2) >= -1;

    }

    /**
     *
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
            positionList.add(new Position(position.getRow(), position.getColumn() + 1 ));
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
     *
     * @return a List of adjacent and "level compatible" positions to the worker's position
     */
    public List<Position> getPossibleMoves() {

        List<Position> positionList = new ArrayList<Position>();
        Position rotatingPosition = new Position(0,0);

        //Check all adjacent positions clockwise
        if (position.getColumn() - 1 >= 0 && position.getRow() - 1 >= 0) {  //UP LEFT
            rotatingPosition.setColumn(position.getColumn() - 1);
            rotatingPosition.setRow(position.getRow() - 1);
            if(checkLevel(rotatingPosition, position))
                 positionList.add(rotatingPosition);
        }
        if (position.getRow() - 1 >= 0) { //UP
            rotatingPosition.setColumn(position.getColumn());
            rotatingPosition.setRow(position.getRow() - 1);
            if(checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getColumn() + 1 < 5 && position.getRow() - 1 >= 0) {//UP RIGHT
            rotatingPosition.setColumn(position.getColumn() + 1);
            rotatingPosition.setRow(position.getRow() - 1);
            if(checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getColumn() + 1 < 5) {//RIGHT
            rotatingPosition.setColumn(position.getColumn() + 1);
            rotatingPosition.setRow(position.getRow());
            if (checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getColumn() + 1 < 5 && position.getRow() + 1 < 5) {//DOWN RIGHT
            rotatingPosition.setColumn(position.getColumn() + 1);
            rotatingPosition.setRow(position.getRow() + 1);
            if (checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getRow() + 1< 5) {//DOWN
            rotatingPosition.setColumn(position.getColumn());
            rotatingPosition.setRow(position.getRow() + 1);
            if (checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getColumn() - 1 >= 0 && position.getRow() + 1 < 5) {//DOWN LEFT
            rotatingPosition.setColumn(position.getColumn() - 1);
            rotatingPosition.setRow(position.getRow() + 1);
            if (checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }
        if (position.getColumn() - 1 >= 0) {//LEFT
            rotatingPosition.setColumn(position.getColumn() - 1);
            rotatingPosition.setRow(position.getRow());
            if (checkLevel(rotatingPosition, position))
                positionList.add(rotatingPosition);
        }

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