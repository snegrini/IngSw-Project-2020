package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class WinMessage extends Message {
    private static final long serialVersionUID = 4516402749630283459L;

    public WinMessage() {
        super(Game.SERVER_NICKNAME, MessageType.WIN_FX);
    }
}
