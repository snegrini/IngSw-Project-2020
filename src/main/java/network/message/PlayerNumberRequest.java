package network.message;

public class PlayerNumberRequest extends Message {

    private static final long serialVersionUID = -2155556142315548857L;

    public PlayerNumberRequest() {
        super("server", MessageType.PLAYERNUMBER_REQUEST);
    }
}



