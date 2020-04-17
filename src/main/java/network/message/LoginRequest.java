package network.message;

public class LoginRequest extends Message {

    private static final long serialVersionUID = -6343239452500134346L;

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
