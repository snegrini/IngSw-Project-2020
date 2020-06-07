package it.polimi.ingsw.network.message;

public class PersistenceMessage extends Message{
    private static final long serialVersionUID = -3479538577845523016L;
    private boolean persistence;

    public PersistenceMessage(String nickname, boolean persistence) {
        super(nickname, MessageType.PERSISTENCE);
        this.persistence = persistence;
    }

    public boolean getPersistenceFlag() {
        return persistence;
    }

    @Override
    public String toString() {
        return "saveMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", saveMatch=" + persistence +
                '}';
    }
}
