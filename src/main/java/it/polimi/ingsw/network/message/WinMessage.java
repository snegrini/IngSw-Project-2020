package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class WinMessage extends Message {
    private static final long serialVersionUID = 4516402749630283459L;

    public String getWinnerNickname() {
        return winnerNickname;
    }

    private final String winnerNickname;

    public WinMessage(String winnerNickname) {
        super(Game.SERVER_NICKNAME, MessageType.WIN_FX);
        this.winnerNickname = winnerNickname;
    }

    @Override
    public String toString() {
        return "WinMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", winnerNickname='" + winnerNickname + '\'' +
                '}';
    }
}
