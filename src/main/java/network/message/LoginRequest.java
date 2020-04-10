package network.message;

public class LoginRequest extends Message {
    //serialVersionUID

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
