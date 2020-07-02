package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ReducedGod;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.List;

/**
 * Message which contains information about the current status of the match.
 */
public class MatchInfoMessage extends Message {

    private static final long serialVersionUID = -9048130764283419918L;
    private final List<String> activePlayers;
    private final List<ReducedGod> activeGods;
    private final List<Color> activeColors;
    private final String activePlayerNickname;

    public MatchInfoMessage(String senderNickname, MessageType messageType, List<String> activePlayers, List<ReducedGod> activeGods, List<Color> activeColors, String activePlayerNickname) {
        super(senderNickname, messageType);
        this.activePlayers = activePlayers;
        this.activeGods = activeGods;
        this.activeColors = activeColors;
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

    public List<Color> getActiveColors() {
        return activeColors;
    }

    @Override
    public String toString() {
        return "MatchInfoMessage{" +
                "nickname=" + getNickname() +
                ", activePlayers=" + activePlayers +
                ", activeGods=" + activeGods +
                ", activeColors=" + activeColors +
                ", activePlayerNickname=" + activePlayerNickname +
                '}';
    }


}
