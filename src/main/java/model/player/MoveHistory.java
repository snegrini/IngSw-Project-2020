package model.player;

import model.board.Position;

public class MoveHistory {

    private Position lastPosition;
    private boolean hasMovedUp, hasMovedDown, hasMovedFlat;


    public MoveHistory(Position lastPosition) {
        this.lastPosition = lastPosition;
        hasMovedDown=false;
        hasMovedUp=false;
        hasMovedFlat=false;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }

    public void setHasMovedUp() {
        hasMovedUp=true;
        hasMovedDown=false;
        hasMovedFlat=false;
    }

    public void setHasMovedDown() {
        hasMovedUp=false;
        hasMovedDown=true;
        hasMovedFlat=false;
    }

    public void setHasMovedFlat() {
        hasMovedUp=false;
        hasMovedDown=false;
        hasMovedFlat=true;
    }


    public Position getLastPosition() {
        return lastPosition;
    }

    public boolean isHasMovedUp() {
        return hasMovedUp;
    }

    public boolean isHasMoveDown() {
        return hasMoveDown;
    }

    public boolean isHasMoveFlat() {
        return hasMoveFlat;
    }
}
