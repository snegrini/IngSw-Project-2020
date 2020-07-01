package it.polimi.ingsw.network.message;

/**
 * Message used by the client to request a login to the server.
 */
public class LoginRequest extends Message {

    private static final long serialVersionUID = -6343239452500134346L;

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "nickname=" + getNickname() +
                '}';
    }
}
