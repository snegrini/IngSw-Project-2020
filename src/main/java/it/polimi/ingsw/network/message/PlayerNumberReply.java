package it.polimi.ingsw.network.message;

/**
 * Message used to send to the server the number of players picked by the client.
 */
public class PlayerNumberReply extends Message {

    private static final long serialVersionUID = -4419241297635925047L;
    private final int playerNumber;

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
                ", playerNumber=" + playerNumber +
                '}';
    }
}
