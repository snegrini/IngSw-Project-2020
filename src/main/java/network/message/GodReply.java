package network.message;

public class GodReply extends Message {

    private static final long serialVersionUID = 8296508558385196591L;
    private int godId;

    public GodReply(String nickname, int godId) {
        super(nickname, MessageType.GOD_REPLY);
        this.godId = godId;
    }

    public int getGodId() {
        return godId;
    }

    @Override
    public String toString() {
        return "GodReply{" +
                "nickname=" + getNickname() +
                ", godId=" + godId +
                '}';
    }
}
