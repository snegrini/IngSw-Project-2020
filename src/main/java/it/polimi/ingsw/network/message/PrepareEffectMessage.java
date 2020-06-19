package it.polimi.ingsw.network.message;

public class PrepareEffectMessage extends Message {
    private static final long serialVersionUID = -2346126431323407902L;
    private final boolean enableEffect;

    public PrepareEffectMessage(String nickname, boolean enableEffect) {
        super(nickname, MessageType.ENABLE_EFFECT);
        this.enableEffect = enableEffect;
    }


    public boolean isEnableEffect() {
        return enableEffect;
    }

    @Override
    public String toString() {
        return "EnableEffect{" +
                "nickname=" + getNickname() +
                ", messageType=" + getMessageType() +
                "enableEffect=" + enableEffect +
                '}';
    }
}
