package network.message;

public class PlayerNumberAck extends  Message {
    // TODO Serial
    private boolean ack;

    public PlayerNumberAck(boolean ack) {
        super("server", MessageType.PLAYERNUMBER_ACK);
        this.ack = ack;
    }


    public boolean isAck() {
        return ack;
    }

    @Override
    public String toString() {
        return "PlayerNumberAck{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "ack=" + ack +
                '}';
    }
}
