package network.message;

import model.board.Position;
import model.player.Worker;

public class Move extends Message {

    private static final long serialVersionUID = 2871961810761631846L;
    private Position orig;
    private Position dest;

    /**
     * Constructor of message from Client to Server for the move command.
     *
     * @param nickname of player
     * @param orig     starting position of the worker to move.
     * @param dest     destination position
     */
    public Move(String nickname, Position orig, Position dest) {
        super(nickname, MessageType.MOVE);
        this.orig = orig;
        this.dest = dest;
    }

    public Position getPositionOrig() {
        return orig;
    }

    public Position getPositionDest() {
        return dest;
    }

    @Override
    public String toString() {
        return "Move{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", orig=" + orig +
                ", dest=" + dest +
                '}';
    }
}
