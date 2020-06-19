package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

import java.util.List;

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
                ", messageType=" + getMessageType() +
                ", nicknameList=" + nicknameList +
                ", numPlayers=" + maxPlayers +
                '}';
    }

}
