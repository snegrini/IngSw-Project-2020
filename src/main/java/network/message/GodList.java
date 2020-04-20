package network.message;

import model.God;

import java.util.List;

public class GodList extends Message {

    private static final long serialVersionUID = -1116045089001448271L;
    private List<God> godList; //used from Server -> Client (and viceversa)
    private God god; //used from Client -> Server


    /**
     * Constructor for GodList request
     *
     * @param godList 9 Gods
     */
    public GodList(List<God> godList) {
        super("server", MessageType.GODLIST);
        this.godList = godList;
    }

    /**
     * Constructor for GodList Reply after first player pick N Gods
     *
     * @param nickname of player
     * @param godList  typically size = #NumPlayer
     */
    public GodList(String nickname, List<God> godList) {
        super(nickname, MessageType.GODLIST);
        this.godList = godList;
    }

    /**
     * Constructor for GodList Reply after player pick his own God
     *
     * @param nickname of player
     * @param god      picked by player
     */
    public GodList(String nickname, God god) {
        super(nickname, MessageType.GODLIST);
        this.god = god;
    }


    public List<God> getGodList() {
        return godList;
    }

    public God getGod() {
        return god;
    }

    @Override
    public String toString() {
        return "GodList{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", godList=" + godList +
                ", god=" + god +
                '}';
    }
}
