package network.message;

import model.board.ReducedSpace;

import java.util.Arrays;

public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private ReducedSpace board[][];

    public BoardMessage(String nickname, MessageType messageType, ReducedSpace[][] board) {
        super(nickname, messageType);
        this.board = board;
    }

    public ReducedSpace[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "BoardMessage{" +
                "board=" + Arrays.toString(board) +
                '}';
    }
}
