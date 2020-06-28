package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.ReducedSpace;

import java.util.Arrays;

public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private final ReducedSpace[][] board;


    public BoardMessage(ReducedSpace[][] board) {
        super(Game.SERVER_NICKNAME, MessageType.BOARD);
        this.board = board;

    }

    public ReducedSpace[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "BoardMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "board=" + Arrays.toString(board) +
                '}';
    }
}
