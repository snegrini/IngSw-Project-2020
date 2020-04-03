package model.player;

import model.Game;
import model.board.Board;
import model.board.Position;
import model.effects.Effect;
import model.enumerations.Color;

import java.util.ArrayList;
import java.util.List;

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
     * Checks the level of the spaces at the given positions.
     *
     * @param position1 position of the first space
     * @param position2 position of the second space
     * @return {@code true} if the difference of spaces' levels is not greater than 1, {@code false} otherwise.
     */
    public boolean checkLevel(Position position1, Position position2) {
        Board board = Game.getInstance().getBoard();
        int level1 = board.getSpace(position1).getLevel();
        int level2 = board.getSpace(position2).getLevel();

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