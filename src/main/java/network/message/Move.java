package network.message;

import model.board.Position;
import model.player.Worker;

public class Move extends Message {

    private static final long serialVersionUID = 2871961810761631846L;
    private Worker worker;
    private Position position;

    /**
     * Constructor of message from Client to Server for the move command.
     *
     * @param nickname of player
     * @param worker   player which is moving
     * @param position destination position
     */
    public Move(String nickname, Worker worker, Position position) {
        super(nickname, MessageType.MOVE);
        this.worker = worker;
        this.position = position;
    }


    public Worker getWorker() {
        return worker;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Move{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", worker=" + worker +
                ", position=" + position +
                '}';
    }
}
