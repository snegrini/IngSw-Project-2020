package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * Message to notify a disconnection to the other players.
 */
public class DisconnectionMessage extends Message {

    private static final long serialVersionUID = -5422965079989607600L;

    private final String nicknameDisconnected;
    private final String messageStr;

    public DisconnectionMessage(String nicknameDisconnected, String messageStr) {
        super(Game.SERVER_NICKNAME, MessageType.DISCONNECTION);
        this.nicknameDisconnected = nicknameDisconnected;
        this.messageStr = messageStr;
    }

    public String getNicknameDisconnected() {
        return nicknameDisconnected;
    }

    public String getMessageStr() {
        return messageStr;
    }

    @Override
    public String toString() {
        return "DisconnectionMessage{" +
                "nicknameDisconnected='" + nicknameDisconnected + '\'' +
                ", messageStr='" + messageStr + '\'' +
                '}';
    }
}
