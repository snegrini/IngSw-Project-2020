package network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    //serialVersionUID

    private final String nickname;
    private final MessageType messageType;

    Message(String nickname, MessageType messageType) {
        this.nickname = nickname;
        this.messageType = messageType;
    }

    public String getNickname() {
        return nickname;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    //implements to string

}
