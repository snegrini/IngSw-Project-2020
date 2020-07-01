package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

/**
 * Message used to confirm or discard a login request of a client.
 */
public class LoginReply extends Message {

    private static final long serialVersionUID = -1423312065079102467L;
    private final boolean nicknameAccepted;
    private final boolean connectionSuccessful;

    public LoginReply(boolean nicknameAccepted, boolean connectionSuccessful) {
        super(Game.SERVER_NICKNAME, MessageType.LOGIN_REPLY);
        this.nicknameAccepted = nicknameAccepted;
        this.connectionSuccessful = connectionSuccessful;
    }

    public boolean isNicknameAccepted() {
        return nicknameAccepted;
    }

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

    @Override
    public String toString() {
        return "LoginReply{" +
                "nickname=" + getNickname() +
                ", nicknameAccepted=" + nicknameAccepted +
                ", connectionSuccessful=" + connectionSuccessful +
                '}';
    }
}
