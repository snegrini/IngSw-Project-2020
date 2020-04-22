package network.message;

import model.board.Position;

public class Build extends Message {
    private static final long serialVersionUID = -5330536550018687004L;
    private Position position;

    /**
     * Constructor of message sent from Client to Server for the build command.
     *
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
