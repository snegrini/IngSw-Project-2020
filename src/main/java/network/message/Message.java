package network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6589184250663958343L;

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

    @Override
    public String toString() {
        return "Message{" +
                "nickname='" + nickname + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
