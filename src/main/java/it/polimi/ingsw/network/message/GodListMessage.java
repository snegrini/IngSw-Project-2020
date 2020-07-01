package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ReducedGod;

import java.util.List;

/**
 * Message which contains the gods available or the god picked from a user.
 */
public class GodListMessage extends Message {

    private static final long serialVersionUID = -1116045089001448271L;
    private final List<ReducedGod> godList;
    private final int request;

    /**
     * Default constructor.
     *
     * @param nickname the nickname of the player.
     * @param godList  the list of god to be sent.
     * @param request  how many gods are requested (will be only 1 after first exchange).
     */
    public GodListMessage(String nickname, List<ReducedGod> godList, int request) {
        super(nickname, MessageType.GODLIST);
        this.godList = godList;
        this.request = request;
    }

    public List<ReducedGod> getGodList() {
        return godList;
    }

    public int getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "GodList{" +
                "nickname=" + getNickname() +
                ", godList=" + godList +
                ", request=" + request +
                '}';
    }
}
