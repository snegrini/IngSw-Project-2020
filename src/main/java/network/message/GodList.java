package network.message;

import model.God;

import java.util.List;

public class GodList extends Message {

    private static final long serialVersionUID = -1116045089001448271L;
    private List<God> godList;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player.
     * @param godList  the list of god to be sent.
     */
    public GodList(String nickname, List<God> godList) {
        super(nickname, MessageType.GODLIST);
        this.godList = godList;
    }

    public List<God> getGodList() {
        return godList;
    }

    @Override
    public String toString() {
        return "GodList{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", godList=" + godList +
                '}';
    }
}
