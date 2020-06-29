package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.board.Position;

import java.util.List;

public class PositionMessage extends Message {

    private static final long serialVersionUID = 5786732391244362819L;
    private final List<Position> positionList;


    /**
     * Contains a list of positions.
     *
     * @param nickname     of Player.
     * @param messageType  type of the Message.
     * @param positionList list of Positions.
     */
    public PositionMessage(String nickname, MessageType messageType, List<Position> positionList) {
        super(nickname, messageType);
        this.positionList = positionList;
    }


    @Override
    public String toString() {
        return "PositionMessage{" +
                "nickname=" + getNickname() +
                ", positionList=" + positionList +
                '}';
    }

    public List<Position> getPositionList() {
        return positionList;
    }
}
