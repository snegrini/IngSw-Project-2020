package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

import java.util.List;

/**
 * Message which contains the current players and the maximum size of the lobby .
 */
public class LobbyMessage extends Message {
    private static final long serialVersionUID = -6886305903361404798L;
    private final List<String> nicknameList;
    private final int maxPlayers;

    public LobbyMessage(List<String> nicknameList, int maxPlayers) {
        super(Game.SERVER_NICKNAME, MessageType.LOBBY);
        this.nicknameList = nicknameList;
        this.maxPlayers = maxPlayers;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String toString() {
        return "LobbyMessage{" +
                "nickname=" + getNickname() +
                ", nicknameList=" + nicknameList +
                ", numPlayers=" + maxPlayers +
                '}';
    }

}
