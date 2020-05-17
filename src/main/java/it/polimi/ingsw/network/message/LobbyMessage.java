package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;

import java.util.List;

public class LobbyMessage extends Message {
    private static final long serialVersionUID = -6886305903361404798L;
    List<String> nicknameList;
    int numPlayers;

    public LobbyMessage(List<String> nicknameList, int numPlayers) {
        super(Game.SERVER_NICKNAME, MessageType.LOBBY);
        this.nicknameList = nicknameList;
        this.numPlayers = numPlayers;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public String toString() {
        return "LobbyMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", nicknameList=" + nicknameList +
                ", numPlayers=" + numPlayers +
                '}';
    }
}
