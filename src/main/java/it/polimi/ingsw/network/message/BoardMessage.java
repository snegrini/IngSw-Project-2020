package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.ReducedSpace;

import java.util.Arrays;

/**
 * Message which contains the board. Used to send the updated board to the client.
 */
public class BoardMessage extends Message {
    private static final long serialVersionUID = -8014575220371739730L;
    private final ReducedSpace[][] board;

    /**
     * Default constructor.
     *
     * @param board The board to wrapped in the message.
     */
    public BoardMessage(ReducedSpace[][] board) {
        super(Game.SERVER_NICKNAME, MessageType.BOARD);
        this.board = board;

    }

    /**
     * Returns the board contained in the message.
     *
     * @return a matrix of {@link it.polimi.ingsw.model.board.ReducedSpace}.
     */
    public ReducedSpace[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "BoardMessage{" +
                "board=" + Arrays.deepToString(board) +
                '}';
    }
}
