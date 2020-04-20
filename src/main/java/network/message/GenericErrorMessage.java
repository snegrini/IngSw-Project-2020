package network.message;

public class GenericErrorMessage extends Message {
    // TODO serialVersionUID

    private final String message;


    public GenericErrorMessage(String message) {
        super("server", MessageType.GENERIC_ERROR_MESSAGE);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "GenericErrorMessage{" +
                "messageType=" + getMessageType() +
                "message='" + message + '\'' +
                '}';
    }
}
