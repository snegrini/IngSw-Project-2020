package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.ReducedGod;

import java.util.List;

public class PlayersMessage extends Message {
    private List<String> nicknameList;
    private List<ReducedGod> godList;

    public PlayersMessage(String nickname, List<String> nicknameList, List<ReducedGod> godList) {
        super(nickname, MessageType.PLAYERS_LIST);
        this.nicknameList = nicknameList;
        this.godList = godList;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public List<ReducedGod> getGodList() {
        return godList;
    }

    @Override
    public String toString() {
        return "PlayersMessage{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                ", nicknameList=" + nicknameList +
                ", godList=" + godList +
                '}';
    }
}
