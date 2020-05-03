package it.polimi.ingsw.PSP016.network.message;

import it.polimi.ingsw.PSP016.model.Game;

public class PlayerNumberRequest extends Message {

    private static final long serialVersionUID = -2155556142315548857L;

    public PlayerNumberRequest() {
        super(Game.serverNickname, MessageType.PLAYERNUMBER_REQUEST);
    }
}



