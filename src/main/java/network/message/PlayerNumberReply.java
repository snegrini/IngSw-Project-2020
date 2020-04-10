package network.message;

public class PlayerNumberReply extends Message {
    //TODO serialVersionUID

    private int playerNumber;

    public PlayerNumberReply(String nickname, int playerNumber) {
        super(nickname, MessageType.PLAYERNUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }


    @Override
    public String toString() {
        return "PlayerNumberReply{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", playerNumber=" + playerNumber +
                '}';
    }
}
