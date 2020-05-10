package it.polimi.ingsw.network.message;

public class DisconnectionMessage extends Message {

    private static final long serialVersionUID = -5422965079989607600L;

    public DisconnectionMessage(String nickname) {
        super(nickname, MessageType.DISCONNECTION);
    }
}
