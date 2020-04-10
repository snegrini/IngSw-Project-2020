package network.message;

import model.board.Position;

public class Build extends  Message {
    //TODO serialVersionUID

    private Position position;

    /**
     * Constructor of message from Client -> Server for Build
     * @param nickname of player
     * @param position of new build
     */
    public Build(String nickname, Position position) {
        super(nickname, MessageType.BUILD);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Build{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", position=" + position +
                '}';
    }
}
