package network.message;

public class LoginReply extends Message {
    //TODO serialVersionUID

    private boolean nicknameAccepted;


    public LoginReply(boolean nicknameAccepted) {
        super("", MessageType.LOGIN_REPLY);
        this.nicknameAccepted = nicknameAccepted;
    }

    public boolean isNicknameAccepted() {
        return nicknameAccepted;
    }

    @Override
    public String toString() {
        return "LoginReply{" +
                "messageType=" + getMessageType() +
                "nicknameAccepted=" + nicknameAccepted +
                '}';
    }
}
