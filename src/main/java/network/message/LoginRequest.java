package network.message;

public class LoginRequest extends Message {
    //TODO serialVersionUID

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

}
