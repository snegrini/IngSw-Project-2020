package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

public class PlayerNumberRequest extends Message {

    private static final long serialVersionUID = -2155556142315548857L;

    public PlayerNumberRequest() {
        super(Game.SERVER_NICKNAME, MessageType.PLAYERNUMBER_REQUEST);
    }
}



