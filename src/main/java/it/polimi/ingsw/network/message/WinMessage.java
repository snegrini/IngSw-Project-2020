package it.polimi.ingsw.network.message;

public class WinMessage extends Message {
    private static final long serialVersionUID = 4516402749630283459L;

    public WinMessage(String nickname) {
        super(nickname, MessageType.WIN_FX);
    }
}
