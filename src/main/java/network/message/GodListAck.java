package network.message;

public class GodListAck extends Message {
    // TODO Serial
    private boolean ack;

    public GodListAck(boolean ack) {
        super("server", MessageType.GODLIST_ACK);
        this.ack = ack;
    }


    public boolean isAck() {
        return ack;
    }

    @Override
    public String toString() {
        return "GodListAck{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "ack=" + ack +
                '}';
    }
}
