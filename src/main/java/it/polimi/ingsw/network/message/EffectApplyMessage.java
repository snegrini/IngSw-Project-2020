package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.player.ReducedWorker;

public class EffectApplyMessage extends Message {

    private ReducedWorker worker;

    public EffectApplyMessage(String nickname, MessageType messageType) {
        super(nickname, messageType);
    }

    public ReducedWorker getWorker() {
        return worker;
    }

    public void setWorker(ReducedWorker worker) {
        this.worker = worker;
    }
}
