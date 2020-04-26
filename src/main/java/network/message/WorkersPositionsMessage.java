package network.message;

import model.board.Position;

import java.util.List;

public class WorkersPositionsMessage extends Message {
    // TODO serial

    private List<Position> positionList;

    public WorkersPositionsMessage(String nickname, List<Position> positionList) {
        super(nickname, MessageType.INIT_WORKERSPOSITIONS);
        this.positionList = positionList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    @Override
    public String toString() {
        return "WorkersPositionsMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", positionList=" + positionList +
                '}';
    }
}
