package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ReducedGod;

import java.util.List;

public class MatchInfoMessage extends Message {

    private static final long serialVersionUID = -2011506753457265907L;
    private List<String> activePlayers;
    private List<ReducedGod> activeGods;
    private String activePlayerNickname;

    public MatchInfoMessage(List<String> activePlayers, List<ReducedGod> activeGods, String activePlayerNickname) {
        super(Game.SERVER_NICKNAME, MessageType.MATCH_INFO);
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
