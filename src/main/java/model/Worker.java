package model;

import org.graalvm.compiler.virtual.phases.ea.EffectList;

import java.util.ArrayList;
import java.util.List;

public class Worker {

    private static Color color;
    private Position position;
    private MoveHistory moveHistory;
    private List<Effect> effects;

    public Worker(Position position, List<Effect> effects) {
        this.position = position;
        this.moveHistory = new MoveHistory(position);
        this.effects = effects;
    }

    public void build(Position p){
        //calls controller.
    }

    /**
     *
     * @param p a valid position to move
     */
    public void move(Position p){

        updateMoveHistory(this.position);
        this.position=p; //worker is now in the new position
    }

    /**
     *
     * @return a List of adjacent positions to the worker's position
     */
    public List<Position> getRange(){

        List<Position> positionList = new ArrayList<Position>();

        if(position.getColumn()-1>=0 && position.getRow()-1>=0)
            positionList.add(new Position(position.getRow() - 1, position.getColumn() - 1));
        if(position.getRow()-1>=0)
            positionList.add(new Position(position.getRow()-1, position.getColumn()));
        if(position.getColumn()+1<=5 && position.getRow()-1>=0)
            positionList.add(new Position(position.getRow()-1, position.getColumn()+1));
        if(position.getColumn()+1<=5)
            positionList.add(new Position(position.getRow(), position.getColumn()+1));
        if(position.getColumn()+1<=5 && position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()+1));
        if(position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()));
        if(position.getColumn()-1>=0 && position.getRow()+1<=5)
            positionList.add(new Position(position.getRow()+1, position.getColumn()-1));
        if(position.getColumn()-1>=0)
            positionList.add(new Position(position.getRow(), position.getColumn()-1));

        return positionList;
    }


    /**
     *
     * @param p is the worker's position in the previous turn
     */
    private void updateMoveHistory(Position p){
        moveHistory.setLastPosition(p);
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
