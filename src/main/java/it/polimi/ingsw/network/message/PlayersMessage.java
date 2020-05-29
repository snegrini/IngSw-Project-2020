package it.polimi.ingsw.network.message;

import java.util.List;

public class PlayersMessage extends Message {
    private List<String> nicknameList;

    public PlayersMessage(String nickname, List<String> nicknameList) {
        super(nickname, MessageType.PLAYERS_LIST);
        this.nicknameList = nicknameList;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public void setNicknameList(List<String> nicknameList) {
        this.nicknameList = nicknameList;
    }

    @Override
    public String toString() {
        return "PlayersMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", nicknameList=" + nicknameList +
                '}';
    }
}
