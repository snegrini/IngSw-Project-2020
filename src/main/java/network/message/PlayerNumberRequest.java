package network.message;

public class PlayerNumberRequest extends Message {
    //TODO serialVersionUID

    public PlayerNumberRequest() {
        super("server", MessageType.PLAYERNUMBER_REQUEST);
    }
}



