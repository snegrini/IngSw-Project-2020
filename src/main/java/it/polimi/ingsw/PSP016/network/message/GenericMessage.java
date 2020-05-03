package it.polimi.ingsw.PSP016.network.message;

import it.polimi.ingsw.PSP016.model.Game;

public class GenericMessage extends Message {
    private static final long serialVersionUID = 934399396584368694L;

    private final String message;


    public GenericMessage(String message) {
        super(Game.serverNickname, MessageType.GENERIC_MESSAGE);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GenericErrorMessage{" +
                "messageType=" + getMessageType() +
                "message='" + message + '\'' +
                '}';
    }
}
