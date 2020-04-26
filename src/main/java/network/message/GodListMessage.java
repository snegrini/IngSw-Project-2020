package network.message;

import model.ReducedGod;

import java.util.List;

public class GodListMessage extends Message {

    private static final long serialVersionUID = -1116045089001448271L;
    private List<ReducedGod> godList;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player.
     * @param godList  the list of god to be sent.
     */
    public GodListMessage(String nickname, List<ReducedGod> godList) {
        super(nickname, MessageType.GODLIST);
        this.godList = godList;
    }

    public List<ReducedGod> getGodList() {
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
