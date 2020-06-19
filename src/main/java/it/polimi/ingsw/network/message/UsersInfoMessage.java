package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ReducedGod;

import java.util.List;

public class UsersInfoMessage extends Message {

    private static final long serialVersionUID = -2011506753457265907L;
    private final List<String> activePlayers;
    private final List<ReducedGod> activeGods;
    private final String activePlayerNickname;

    public UsersInfoMessage(String senderNickname, MessageType messageType, List<String> activePlayers, List<ReducedGod> activeGods, String activePlayerNickname) {
        super(senderNickname, messageType);
        this.activePlayers = activePlayers;
        this.activeGods = activeGods;
        this.activePlayerNickname = activePlayerNickname;
    }

    public String getActivePlayerNickname() {
        return activePlayerNickname;
    }

    public List<String> getActivePlayers() {
        return activePlayers;
    }

    public List<ReducedGod> getActiveGods() {
        return activeGods;
    }

    @Override
    public String toString() {
        return "MatchInfoMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "activePlayers=" + activePlayers +
                ", activeGods=" + activeGods +
                ", activePlayerNickname='" + activePlayerNickname + '\'' +
                '}';
    }


}
